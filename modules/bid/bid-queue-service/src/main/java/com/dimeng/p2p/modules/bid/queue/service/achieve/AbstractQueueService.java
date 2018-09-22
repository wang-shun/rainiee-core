package com.dimeng.p2p.modules.bid.queue.service.achieve;

import com.dimeng.framework.cache.redis.jedispool.RedisPooledConnectionProvider;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.service.AbstractP2PService;
import redis.clients.jedis.Jedis;

public abstract class AbstractQueueService extends AbstractP2PService
{
    
    public AbstractQueueService(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    protected static final int DECIMAL_SCALE = 9;

    public Jedis getJedis(){
        return (this.serviceResource.getDataConnectionProvider(RedisPooledConnectionProvider.class, "redis.pool.provider")).getJedisClient();
    }

}
