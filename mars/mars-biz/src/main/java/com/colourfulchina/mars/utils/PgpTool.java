package com.colourfulchina.mars.utils;

public class PgpTool {
	
	/**
	 * 自动生成加密后的 附带目录文件名
	 * @param sourceName  附带目录的文件名。
	 * @return
	 */
	public static String DestFileName(String sourceName) {
		int l = sourceName.lastIndexOf("/");
		int length = sourceName.length();
		
	    String direc= sourceName.substring(0, l+1);
		String fName = sourceName.substring(l + 1, length);
		

		/*fName = fName.indexOf(".") > 0 ? 
				fName.substring(0, fName.lastIndexOf("."))
				: fName;*/
		
		return direc + fName+".DAT";
		
	}

	
	/**
	 * 
	 * @param sourceName
	 * @return
	 */
	public static String DestFileName2(String sourceName) {
		int l = sourceName.lastIndexOf("/");
		int length = sourceName.length();
		
	    String direc= sourceName.substring(0, l+1);
		String fName = sourceName.substring(l + 1, length);
		
		
		/**
		 * 修改，改成直接在后边添加.PGP
		 */
//		fName = fName.indexOf(".") > 0 ? 
//				fName.substring(0, fName.lastIndexOf("."))
//				: fName;
		
		return direc + fName+".pgp";
		
	}

}
