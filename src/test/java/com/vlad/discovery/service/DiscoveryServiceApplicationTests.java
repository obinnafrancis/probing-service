package com.vlad.discovery.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = {"classpath:application-vlad.properties"})
class DiscoveryServiceApplicationTests {

	@Test
	public void validation() {
		TimeZone.setDefault(TimeZone.getTimeZone("Africa/Lagos"));
		String token = getDummyExpiredToken();
		String key = "exp";
		boolean checkResult = validateTokenExpiry(token,key);
		Assert.assertFalse(checkResult);
	}
	public boolean validateTokenExpiry (String accessToken, String key){
		String payload = new String(Base64.getDecoder().decode(accessToken.split("\\.")[1]));
		TypeReference<HashMap<String, Object>> typeReference = new TypeReference<HashMap<String, Object>>() {};
		Map<String, Object> map = new Gson().fromJson(payload,typeReference.getType());
		LocalDateTime expiryDateTime = Instant.ofEpochSecond((long) ((double) map.get(key)))
				.atZone(ZoneId.systemDefault()).toLocalDateTime();
		if(expiryDateTime.isAfter(LocalDateTime.now())){
			return true;
		}
		return false;
	}
	public String getDummyExpiredToken() {
		return "eyJhbGciOiJSUzI1NiJ9.eyJhdWQiOlsiYXBpLWdhdGV3YXkiLCJjYXJkbGVzcy1zZXJ2aWNlIiwiZmluY2gtb25ib2FyZGluZy1zZXJ2aWNlIiwiZmluY2gtcGxhdGZvcm0tc2VydmljZSIsImZpbmNoLXRyYW5zYWN0aW9uLXNlcnZpY2UiLCJmaW5jaC11c2VyLW1nbXQtc2VydmljZSIsImlzdy1jb3JlIiwicGFzc3BvcnQiLCJ3YWxsZXQiXSwic2NvcGUiOlsicHJvZmlsZSJdLCJleHAiOjE2MzU4NDcxOTksImNsaWVudF9uYW1lIjoiRklOQ0ggVHJhbnNhY3Rpb24tUHJvZCIsImNsaWVudF9sb2dvIjpudWxsLCJqdGkiOiJmNDg4MWUxYi1iNGIyLTQxYWEtYTQ5Zi02YWUzNDYwY2EyMDIiLCJjbGllbnRfZGVzY3JpcHRpb24iOm51bGwsImNsaWVudF9pZCI6IklLSUExOTA0NzcxQjI0MDJDMDhCNUIyMUU1REFDNTlGQkNGNEIxQkExMkE4In0.TBz5V50YuO81e_EpC8tkrnDEG1x650nwWfzmEoHTzCUm3svsMXJZynosq0D0SDLrp0QlZPKcvExlP7p-aXgvHihJQCoYVuDdtTE-5fX9gkl-kCjlyj774-RXmvQOQLOzK6P3Y2v7xpU2z9sCLOFw-5W8cRwxoxXnxK_Dp7qnc2AIjqX5aMWF0lGq4TBkQKOCvtDpFJyo9FngSmwvtOA_oHJLZIn6_vvtAk0baUHKtaWgYIRRhrUc1w_Y8uGDLrQonTFrb28akim4gouYo2n2j_J6tjQhAAm7pEAqaTDj1gUvIN0lEyZCatBsZmjoOQbeaUs9f5Upt8zCE4YbhCOuAA";
	}

}
