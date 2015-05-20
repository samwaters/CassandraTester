package sam;

import java.util.Random;

public class PutThread extends BaseThread
{
	public PutThread(String threadName, String host, String keyspace)
	{
		super(threadName, host, keyspace);
	}
	
	public void run()
	{
		int operationCount = 0;
		long nextPrintTime = System.currentTimeMillis() + 5000;
		Random r = new Random();
		while(this.canRun)
		{
			String rowKey = this.threadName + "-row-" + r.nextInt(1000000);
			this.session.execute("INSERT INTO samTest (key, value1, value2, value3) VALUES('" + rowKey + "', 'This is Value 1', 'This is Value 2', 'This is Value 3')");
			operationCount++;
			if(System.currentTimeMillis() >= nextPrintTime)
			{
				System.out.println(this.threadName + " : " + (operationCount / 5) + " writes/s");
				operationCount = 0;
				nextPrintTime = System.currentTimeMillis() + 5000; 
			}
		}
		this.closeConnections();
	}
}
