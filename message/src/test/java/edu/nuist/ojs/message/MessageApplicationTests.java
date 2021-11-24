package edu.nuist.ojs.message;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import com.opencsv.CSVReader;

import edu.nuist.ojs.message.util.LogStatusMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import edu.nuist.ojs.message.controller.UpLoadAndReadCSVApi;

import edu.nuist.ojs.message.redis.*;

@SpringBootTest
class MessageApplicationTests {
	@Autowired
	LogStatusMapper logStatusMapper;

	@Autowired
    RedisRouter redisRouter;

	@Test
	void contextLoads() {
		System.out.println(redisRouter.get("test"));
	}


	@Autowired
	private UpLoadAndReadCSVApi csvapi;

	@Test
	public void t() throws UnsupportedEncodingException, IOException{
		try {
			URL url =new URL("https://hnajin-1306618516.cos.ap-nanjing.myqcloud.com/LogSearchResults-20210804-2109.csv");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(20 *1000);
			CSVReader reader = new CSVReader( new InputStreamReader(conn.getInputStream(), "UTF-8") );
			String[] strs;
			while ((strs = reader.readNext()) != null) {
				for(String s : strs) System.out.print(s + "  ");
				System.out.println(  );
			}
			reader.close();
			
		}catch(Exception e){

		}
	}


}
