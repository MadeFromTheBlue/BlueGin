package blue.made.bluegin.core.asset;

import java.io.InputStream;

public interface DataSourceAsset extends Asset<InputStream> {
	/**
	 * Provide a new inputstream for reading the data.
	 */
	@Override
	InputStream getData();
}