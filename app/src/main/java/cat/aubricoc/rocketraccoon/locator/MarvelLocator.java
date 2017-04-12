package cat.aubricoc.rocketraccoon.locator;

import com.canteratech.restclient.Request;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import cat.aubricoc.rocketraccoon.locator.response.MarvelComicsResponse;
import cat.aubricoc.rocketraccoon.model.Comic;

public class MarvelLocator {

	private MarvelLocator() {
		super();
	}

	public static MarvelLocator newInstance() {
		return new MarvelLocator();
	}

	public List<Comic> getComics(Integer characterId) {
		return req().addPath("v1/public/characters/" + characterId + "/comics").get().getEntity(MarvelComicsResponse.class).getData().getResults();
	}

	public Comic getComic(Integer comicId) {
		return req().addPath("v1/public/comics/" + comicId).get().getEntity(MarvelComicsResponse.class).getData().getResults().get(0);
	}

	private Request req() {
		String privateKey = "0f1d0fdf46a0bf32f962b0b9997233c0395cdf8e";
		String publicKey = "6a7ed890b4b941a925202a5630d5b162";
		Long ts = new Date().getTime();
		String hash;
		try {
			byte[] digest = MessageDigest.getInstance("MD5").digest((ts + privateKey + publicKey).getBytes());
			hash = String.format("%032x", new BigInteger(1, digest));
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("Cannot digest MD5 hash", e);
		}
		return Request.newInstance("https://gateway.marvel.com").addParam("apikey", publicKey).addParam("ts", ts).addParam("hash", hash);
	}
}
