package performance;

import com.opensymphony.oscache.base.events.CacheEntryEvent;
import com.opensymphony.oscache.base.events.CacheEntryEventListener;
import com.opensymphony.oscache.base.events.CacheGroupEvent;
import com.opensymphony.oscache.base.events.CachePatternEvent;
import com.opensymphony.oscache.base.events.CachewideEvent;

public class MyListener implements CacheEntryEventListener {

	public void cacheEntryAdded(CacheEntryEvent arg0)
	{
		System.out.println("Added to cache " + arg0.getKey());

	}

	public void cacheEntryFlushed(CacheEntryEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	public void cacheEntryRemoved(CacheEntryEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	public void cacheEntryUpdated(CacheEntryEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	public void cacheFlushed(CachewideEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	public void cacheGroupFlushed(CacheGroupEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	public void cachePatternFlushed(CachePatternEvent arg0)
	{
		// TODO Auto-generated method stub

	}

}