package DP_Disambiguation_DumpHandler;

public class Printer {

	public final static void print(String message, String progress)
	{
		clearConsole();
		System.out.flush();
		System.out.println(message);
		System.out.println(progress);
	}
	public final static void clearConsole()
	{
	    try
	    {
	        final String os = System.getProperty("os.name");

	        if (os.contains("Windows"))
	        {
	            Runtime.getRuntime().exec("cls");
	        }
	        else
	        {
	            Runtime.getRuntime().exec("clear");
	        }
	    }
	    catch (final Exception e)
	    {
	        //  Handle any exceptions.
	    }
	}
	
}
