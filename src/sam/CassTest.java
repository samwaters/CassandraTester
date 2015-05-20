package sam;

import java.util.ArrayList;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Cluster.Builder;
import com.datastax.driver.core.Session;

public class CassTest
{
	public static void main(String[] args)
	{
		if(args.length < 4)
		{
			System.out.println("Usage: CassTest.jar <host> <keyspace> <mode> <threads>");
			return;
		}
		String host = args[0];
		String keyspace = args[1];
		String mode = args[2];
		if(!mode.equals("create") && !mode.equals("get") && !mode.equals("put"))
		{
			System.out.println("Invalid mode");
			return;
		}
		if(mode.equals("create"))
		{
			System.out.println("Creating table samTest...");
			Builder builder = Cluster.builder();
			builder.addContactPoint(host);
			Cluster cluster = builder.build();
			Session session = cluster.connect(keyspace);
			session.execute("CREATE TABLE samTest (key varchar PRIMARY KEY, value1 varchar, value2 varchar, value3 varchar)");
			session.close();
			cluster.close();
			return;
		}
		int threadCount = Integer.parseInt(args[3]);
		ArrayList<BaseThread> threads = new ArrayList<BaseThread>();
		if(mode.equals("get"))
		{
			for(int i=0; i<threadCount; i++)
			{
				GetThread t = new GetThread("thread" + i, host, keyspace);
				threads.add(t);
				t.start();
			}
		}
		else
		{
			for(int i=0; i<threadCount; i++)
			{
				PutThread t = new PutThread("thread" + i, host, keyspace);
				threads.add(t);
				t.start();
			}
		}
		long endTime = System.currentTimeMillis() + 30000;
		while(System.currentTimeMillis() < endTime)
		{
			try
			{
				Thread.sleep(1000);
			}
			catch(InterruptedException e)
			{ }
		}
		for(int i=0; i<threads.size(); i++)
		{
			threads.get(i).shutdown(false);
		}
	}
}
