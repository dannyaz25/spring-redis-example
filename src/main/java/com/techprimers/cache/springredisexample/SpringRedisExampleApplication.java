package com.techprimers.cache.springredisexample;

import com.techprimers.cache.springredisexample.model.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.Jedis;

import javax.websocket.Session;
import java.util.Collection;
import java.util.List;
import java.util.Set;


@SpringBootApplication
public class SpringRedisExampleApplication {

	@Bean
	JedisConnectionFactory jedisConnectionFactory() {
		JedisConnectionFactory jedisConFactory
				= new JedisConnectionFactory();
		jedisConFactory.setHostName("127.0.0.1");
		jedisConFactory.setPort(6379);
		return jedisConFactory;
	}
	@Bean
	RedisTemplate<String, User> redisTemplate() {
		RedisTemplate<String, User> redisTemplate = new RedisTemplate<>();
		redisTemplate.setEnableTransactionSupport(true);
		redisTemplate.setConnectionFactory(jedisConnectionFactory());
		return redisTemplate;
 	}

	public static  Collection<Session> getActiveSessions(){
		Jedis jedis = null;

		try {
			jedis =new Jedis("127.0.0.1",6379);
			Set<String> keys = jedis.keys( "*");
			if(CollectionUtils.isEmpty(keys)){
				return null;
			}
			List<String> values = jedis.mget(keys.toArray(new String[keys.size()]));
			return SerializeUtils.deserializeFromStrings(values);
		} catch (Exception e){
		  System.out.print(e);
		} finally {
			jedis.close();
		}
		return null;
	}
	public static void main(String[] args) {
		/*try{
			Jedis jedis=new Jedis("127.0.0.1",6379);
			System.out.println("Connection Successful");
			System.out.println("Set push key value:"+jedis.sadd("apple","chocolate"));
			System.out.println("Set pop key value:"+jedis.spop("apple"));
			System.out.println("all KEYS:"+jedis.flushAll());
		}catch(Exception e){

		}
		*/
		System.out.println(getActiveSessions());

		SpringApplication.run(SpringRedisExampleApplication.class, args);
	}
}
