package com.demo.voice.process.utils;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:voice-search-context-test.xml"})
public class NormalizeTest {
	@Autowired
	private Normalize normalize;
	@Test
	public void normalizeAmount(){
		Assert.assertEquals(151200000, normalize.calculateAmount("151 triệu 200 nghìn"));
		Assert.assertEquals(1200000, normalize.calculateAmount("Tôi muốn chuyển 1 triệu 200 ngàn"));
		Assert.assertEquals(4200000, normalize.calculateAmount("Tôi muốn chuyển bốn triệu 200 ngàn"));
		//Assert.assertEquals(100700125, normalize.normalizeAmount("100 triệu 700 nghìn 215"));
		Assert.assertEquals(1700215, normalize.calculateAmount("Tôi muốn chuyển 1.700.000 215"));
		
		Assert.assertEquals(500000, normalize.calculateAmount("5 lít"));
		Assert.assertEquals(550000, normalize.calculateAmount("5 lít rưỡi"));
	}
}
