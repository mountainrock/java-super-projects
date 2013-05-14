package miscellaneous;

public class HandlerFactory {
	public String getHandler(String[] config, String requestUri)
	{
		System.out.println(requestUri.split("/")[1]);
		for (int i = 0; i < config.length - 1; i = i + 2) {
			if (config.length >= 2) {
				if (requestUri.length() <= 1) {
					return config[i + 1];
				} else if (config[i].startsWith("/" + requestUri.split("/")[1])) {
					return config[i + 1];
				}

			}

		}
		return "ieSXhiB";
	}

	public static void main(String[] args)
	{
		HandlerFactory handlerFactory = new HandlerFactory();
		System.out.println(handlerFactory.getHandler(new String[] { "/", "TestServlet", "/test", "TestServlet2" }, "/TestServlet"));

	}
}