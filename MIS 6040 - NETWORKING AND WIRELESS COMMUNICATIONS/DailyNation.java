/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package daily.nation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.time.LocalDateTime;

/**
 *
 * @author bulivaa
 */
public class DailyNation
{

    private final String BASE_PATH = "http://downloads.realviewtechnologies.com/";
    private final String FOLDER = "Nation Media/Daily Nation/";
    private final String FILE_TYPE = ".pdf";
    //Mar 16th 2015.pdf";

    private String getSuffix(String number)
    {
        String suffix = "th";

        String numberToString = number.trim();
        String onesPosition = numberToString.substring(numberToString.length() - 1);

        switch (onesPosition)
        {
            case "1":
                suffix = "st";
                break;
            case "2":
                suffix = "nd";
                break;
            case "3":
                suffix = "rd";
                break;
            default:
                suffix = "th";
                break;
        }

        return suffix;
    }

    private String getDownloadFilePath() throws Exception
    {
        LocalDateTime timePoint = LocalDateTime.now();     // The current date and time

        String filePath = String.valueOf(timePoint.getMonth()).substring(0, 3).toLowerCase() + " "
                + String.valueOf(timePoint.getDayOfMonth())
                + getSuffix(String.valueOf(timePoint.getDayOfMonth())) + " "
                + String.valueOf(timePoint.getYear());

        Object[] fileName =
        {
            BASE_PATH,
            URLEncoder.encode(FOLDER, "UTF-8").replaceAll("\\+", "%20"),
            URLEncoder.encode(filePath, "UTF-8").replaceAll("\\+", "%20"),
            FILE_TYPE
        };

        return String.format("%s%s%s%s", fileName);

    }

    private void saveNewspaper(String path) throws Exception
    {
        LocalDateTime timePoint = LocalDateTime.now();     // The current date and time

        Object[] fileName =
        {
            String.valueOf(timePoint.getMonth()).substring(0, 3).toLowerCase(),
            String.valueOf(timePoint.getDayOfMonth()),
            getSuffix(String.valueOf(timePoint.getDayOfMonth())),
            String.valueOf(timePoint.getYear()),
            FILE_TYPE
        };

        String canonicalName = String.format("%s %s%s %s%s", fileName);

        //System.out.println("Saving " + path + " to " + canonicalName.toUpperCase());
        System.out.println("Downloading newspaper, please wait...");

        URL url = new URL(path);
        URLConnection urlConnection = url.openConnection();
        urlConnection.connect();
        int fileSize = urlConnection.getContentLength();

        InputStream is = url.openStream();
        OutputStream os = new FileOutputStream(canonicalName.toUpperCase());

        byte[] b = new byte[2048];
        int length;

        double savedSize = 0;
        File localFile = new File(canonicalName.toUpperCase());
        int movement = 0;
        int oldPercentage = 0;

        while ((length = is.read(b)) != -1)
        {
          os.write(b, 0, length);

          if (movement % 10 == 0)
          {
            savedSize = localFile.length();
            int percentage = (int) Math.ceil((savedSize * 100) / fileSize);

//                System.out.print(savedSize
//                        + " of " + fileSize + " (" + (percentage) + "%)");
            
            
            if(percentage != oldPercentage)
            {
                System.out.print((percentage) + "%");
                oldPercentage = percentage;
            }
          }
          else
          {
            if (movement % 35 == 0 && oldPercentage < 100)
              System.out.print(".");
          }

          movement++;
        }

        is.close();
        os.close();
        
        System.out.println();
        System.out.println(canonicalName.toUpperCase() + " saved successfully. Enjoy.");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception
    {
        DailyNation dn = new DailyNation();
        String newspaper = dn.getDownloadFilePath();
        dn.saveNewspaper(newspaper);
    }

}

