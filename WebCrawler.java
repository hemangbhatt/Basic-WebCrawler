// File: WebCrawler.java
// Author: Hemang Bhatt
// CMSC 331 Project



/* WebCrawler Project */


import java.net.*;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class WebCrawler
{
	// variable to count characters
	public static int numChar;
	
	
	// to check the given url is valid
	public static boolean isValid(String address)
	{
		try
		{
			URL url = new URL(address);
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}
		
	
	// This function will fetch the webpage of the given url and return as a string
	// and also count number of character
	public static String getPageSource(String address) throws Exception
	{
		String pageSource = "";
		String inputLine = "";
		
		// make url and then open connection
		URL url = new URL(address);
		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		while ((inputLine = in.readLine()) != null)
		{
			pageSource += inputLine;
		}
		
		in.close();
		
		numChar += pageSource.length();
		return pageSource;
	}
	
	
	// extractLinks takes string input, parses string, and returns string vector
	public static Vector<String> extractLinks(String url) throws Exception
	{
		Vector<String> tempLinks = new Vector<String>();
		
		// Anchor Expression using Regex
		String AnchorRegEx = "<a\\s+(?:[^>]*?\\s+)?href=\"([^\"]*)\"";
		String UrlRegEx = "\"(.*)\"";
		
		Pattern pattern = Pattern.compile(AnchorRegEx);
		Matcher aMatcher = pattern.matcher(getPageSource(url));
		
		Pattern urlPattern = Pattern.compile(UrlRegEx);
		
		while (aMatcher.find())
		{
			String tempAnchor = aMatcher.group();
			Matcher tempMatcher = urlPattern.matcher(tempAnchor);
			if (tempMatcher.find())
				tempLinks.add(tempMatcher.group(1));
		}
		
		return tempLinks;
		
	}
	
	public static void main(String args[]) throws Exception
	{
		String startURL;
		String nextURL;
		int numSitesVisited = 0;
		int MAXSITES = 15;
		int MAXCHARS = 1000000;
		numChar = 0;
		int siteCounter = 0;
		
		// Create Vecotr of Websites to Visit
		Vector<String> sitesVec = new Vector<String>();
		
		// Already visited Websites Vector
		Vector<String> visitedSitesVec = new Vector<String>();
		
		
		
		// if Entered more than 1 input
		if (args.length != 1)
		{
			System.out.println("Enter Website link. Ex. \"http://www.google.com\"" );
			Console console = System.console();
			startURL = console.readLine("Enter Website link: ");
			
		}
		
		// User entered website
		else
			startURL = args[0];
		
		// Print Greetings
		System.out.println("-------WebCrawler Started------");
		System.out.print("Entered Website link--> ");
		System.out.print(startURL);
		System.out.println();
		
		
		sitesVec.add(startURL);
		
		
		
		// loop until char and visitedSites is less than limit
		while (numSitesVisited < MAXSITES && numChar < MAXCHARS && (!sitesVec.isEmpty()))
		{
			
			//System.out.println("Inside first while");
			// visit site from Vectors
			nextURL = sitesVec.firstElement();
			Vector<String> urlVector;
			urlVector = extractLinks(nextURL);
			try
			{
				
				visitedSitesVec.add(sitesVec.firstElement());
				sitesVec.remove(0);
				numSitesVisited++;
			}
			
			catch (Exception e)
			{
				System.out.println("Exception caught "+ e + "\n");
				numSitesVisited--;
			}
			
				
			while (sitesVec.size() <= MAXSITES && !urlVector.isEmpty())
			{
				
				String tempUrl = urlVector.firstElement();
				urlVector.remove(0);
				
				if (isValid(tempUrl) && !sitesVec.contains(tempUrl) && !visitedSitesVec.contains(tempUrl))
				{
					sitesVec.add(tempUrl);
				}
				
				
			}
			
			
			System.out.println("Number of Site:" + numSitesVisited);
			System.out.println("Website Visited : " + nextURL);
			System.out.println("Total Characters: " + numChar);
			
			
			
		}
		
		// WebCrawling Report
		System.out.println("----------WebCrawler Stopped----------");
		System.out.println("********** Results ************");
		System.out.println("Total Characters read: " + numChar);
		System.out.println("Number of websites visited: " + numSitesVisited);
			
		
	}
	
	
	
}
