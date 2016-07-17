package com.caoye.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.caoye.model.Mp3Info;
import com.caoye.mp3player.AppConstant;

public class FileUtils {
	public File createFileInSDCard(String fileName, String dir) throws IOException {
		File file = new File(AppConstant.SDCARD_ROOT+ dir + fileName);
		file.createNewFile();
		return file;
	}

	public File createSDFolder(String folder) {
		File dirFile = new File(AppConstant.SDCARD_ROOT + folder);
		dirFile.mkdirs();
		return dirFile;
	}

	public boolean isFileExist(String fileName,String folder){
		File file = new File(AppConstant.SDCARD_ROOT + folder + fileName);
		return file.exists();
	}

	public File write2SDFromInput(String folder,String fileName,InputStream input){
		File file = null;
		OutputStream output = null;
		try {
			createSDFolder(folder);
			file = createFileInSDCard(fileName, folder);
			output = new FileOutputStream(file);
			byte buffer[] = new byte[4 * 1024];
			int temp ;
			while((temp = input.read(buffer)) != -1){
				output.write(buffer,0,temp);
			}
			output.flush();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				output.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		return file;
	}

	public List<Mp3Info> getMp3Files() {
		List<Mp3Info> infos = new ArrayList<>();
		File file = new File(AppConstant.SDCARD_ROOT + AppConstant.MP3_FOLDER);
		File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].getName().endsWith("mp3")) {
				Mp3Info info = new Mp3Info();
				info.setMp3Name(files[i].getName());
				info.setMp3Size(files[i].length() + "");

				String[] temp = info.getMp3Name().split("\\.");
				String lrcName = temp[0] + ".lrc";
				if (isFileExist(lrcName, AppConstant.LYRICS_FOLDER)) {
					info.setLrcName(lrcName);
				}
				infos.add(info);
			}
		}
		return infos;
	}
}