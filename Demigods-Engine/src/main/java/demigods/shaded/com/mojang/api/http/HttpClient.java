package demigods.shaded.com.mojang.api.http;

import java.io.IOException;
import java.net.Proxy;
import java.net.URL;
import java.util.List;

public interface HttpClient
{
	public String post(URL url, demigods.shaded.com.mojang.api.http.HttpBody body, List<HttpHeader> headers) throws IOException;

	public String post(URL url, Proxy proxy, demigods.shaded.com.mojang.api.http.HttpBody body, List<HttpHeader> headers) throws IOException;
}
