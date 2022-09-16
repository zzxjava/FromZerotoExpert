package com.zzx.FromZerotoExpert;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.zzx.FromZerotoExpert.model.dao")
public class FromZerotoExpertApplication {

	public static void main(String[] args) {
		SpringApplication.run(FromZerotoExpertApplication.class, args);
	}

}
