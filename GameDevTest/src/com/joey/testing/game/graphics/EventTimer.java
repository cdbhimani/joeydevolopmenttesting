package com.joey.testing.game.graphics;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Vector;

public class EventTimer
{
	public Vector<String> name = new Vector<String>();

	public Vector<long[]> time = new Vector<long[]>();

	public void tick(String id)
	{
		time.get(name.indexOf((id)))[1] = System.currentTimeMillis();
	}

	public void mark(String id)
	{
		if (time.contains(id))
		{
			long[] dat = time.get(name.indexOf((id)));
			dat[0] = System.currentTimeMillis();
			dat[1] = 0;
		} else
		{
			time.add(new long[]
			{ System.currentTimeMillis(), 0 });
			name.add(id);
		}
	}

	public float getTime(String id)
	{
		int index = name.indexOf((id));
		if(index < 0){return 0;}
		long[] dat = time.get(index);
		if (dat == null)
		{
			return -1;
		}
		if (dat[1] == 0)
		{
			return (System.currentTimeMillis() - dat[0]);
		} else
		{
			return dat[1] - dat[0];
		}
	}

	public void printData()
	{

		for (int i = 0; i < name.size(); i++)
		{
			System.out.println(name.get(i) + "\t:"
					+ (time.get(i)[1] - time.get(i)[0]));
		}
	}

	public String getTitle()
	{
		String titleOut = "";
		for (int i = 0; i < name.size(); i++)
		{
			titleOut += name.get(i) + ",";
		}

		return titleOut;
	}

	public String getData()
	{

		String timeOut = "";
		for (int i = 0; i < name.size(); i++)
		{
			timeOut += (getTime(name.get(i))) + ",";
		}
		return timeOut;
	}

	public void clear()
	{
		time.clear();
		name.clear();
	}
}
