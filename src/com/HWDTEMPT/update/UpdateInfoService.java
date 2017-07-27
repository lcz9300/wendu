package com.HWDTEMPT.update;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;

public class UpdateInfoService {
	public UpdateInfoService(Context context) {
	}

	public UpdateInfo getUpDateInfo() throws Exception {
		String path = GetServer_updateUrl.getUrl() + "/update.txt";
		StringBuffer sb = new StringBuffer();
		String line = null;
		BufferedReader reader = null;
		try {
			// ����һ��url����
			URL url = new URL(path);
			// ͨ�^url���󣬴���һ��HttpURLConnection�������ӣ�
			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();
			// ͨ��HttpURLConnection���󣬵õ�InputStream
			reader = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			// ʹ��io����ȡ�ļ�
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		String info = sb.toString();
		UpdateInfo updateInfo = new UpdateInfo();
		updateInfo.setVersion(info.split("&")[0]);
		updateInfo.setDescription(info.split("&")[1]);
		updateInfo.setUrl(info.split("&")[2]);
		return updateInfo;
	}

}
