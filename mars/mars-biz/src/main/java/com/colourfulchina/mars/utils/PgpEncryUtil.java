package com.colourfulchina.mars.utils;


import cryptix.message.KeyBundleMessage;
import cryptix.message.MessageFactory;
import cryptix.message.stream.EncryptedMessageOutputStream;
import cryptix.message.stream.LiteralMessageOutputStream;
import cryptix.pki.KeyBundle;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.SecureRandom;
import java.util.Collection;


/**
 * 作 者：sunzg
 *
 * 功能说明：   加密接口类
 *            参数： 需要加密的文件名 、加密后的文件名 和 公钥
 *
 * 日 期：2009-08-25
 *
 * 修改历史：
 *
 */

public class PgpEncryUtil {


	/**
	 * 加密入口  .txt---->.pgp
	 * @param encryFile
	 * @param publicasc
	 * @return
	 * @throws Exception
	 */
	public static String  Encry(String encryFile,	String publicasc ) throws Exception
	{
		encryFile = encryFile.replace('\\', '/');
		String encryedFile = PgpTool.DestFileName(encryFile);

		Encry(encryFile,encryedFile,publicasc);

		return encryedFile;
	}

	/**
	 * .txt---->.txt.pgp
	 * @param encryFile
	 * @param publicasc
	 * @return
	 * @throws Exception
	 */
	public static String  Encry2(String encryFile,	String publicasc ) throws Exception
	{
		encryFile = encryFile.replace('\\', '/');
		String encryedFile = PgpTool.DestFileName2(encryFile);

		Encry(encryFile,encryedFile,publicasc);

		return encryedFile;
	}

	/**
	 * 加密主程序
	 * @param encryFile
	 * @param encryedFile
	 * @param publicasc
	 * @throws Exception
	 */
	public static void Encry(String encryFile, String encryedFile,
			String publicasc) throws Exception  {
		java.security.Security
				.addProvider(new cryptix.jce.provider.CryptixCrypto());
		java.security.Security
				.addProvider(new cryptix.openpgp.provider.CryptixOpenPGP());

		KeyBundle publicBob = null;
		FileInputStream inEncry = null;

		try
		{
			Collection msgs;
			KeyBundleMessage kbm;

			MessageFactory mf = MessageFactory.getInstance("OpenPGP");
			inEncry = new FileInputStream(publicasc);

			msgs = mf.generateMessages(inEncry);
			kbm = (KeyBundleMessage) msgs.iterator().next();

			publicBob = kbm.getKeyBundle();

			inEncry.close();
			inEncry = null;

		}
		catch (Exception me)
		{
			System.err.println("读密钥出错.");
			me.printStackTrace();
			throw me;

		} finally {
			try
			{
				if (inEncry != null)
					inEncry.close();
				inEncry = null;

			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		FileOutputStream fileos = null;
		LiteralMessageOutputStream litmos = null;
		EncryptedMessageOutputStream encmos = null;

		FileInputStream  inputEncryStream = null;
		try
		{
			inputEncryStream =  new FileInputStream(encryFile);
			fileos = new FileOutputStream(encryedFile);

			litmos = LiteralMessageOutputStream.getInstance("OpenPGP");
			encmos = EncryptedMessageOutputStream.getInstance("OpenPGP");

			SecureRandom sr = new SecureRandom();
			litmos.init(encmos, sr);
			encmos.init(fileos, sr);
			encmos.addRecipient(publicBob);


			byte[] buf = new byte[4096];
			int len = inputEncryStream.read(buf);

			while (len > 0)
			{
				litmos.write(buf, 0, len);
				len = inputEncryStream.read(buf);
			}

			litmos.close();
			fileos.close();
			inputEncryStream.close();

			encmos = null;
			litmos = null;
			inputEncryStream = null;
			fileos = null;
			sr = null;

		}
		catch (Exception ioe)
		{
			System.err.println("IO error.");
			ioe.printStackTrace();
			throw ioe;
		}
		finally
		{
			try {
				if (inputEncryStream != null)
					inputEncryStream.close();
				inputEncryStream = null;

				if (fileos != null)
					fileos.close();
				fileos = null;

				if (litmos != null)
					litmos.close();
				litmos = null;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

//	public static void main(String[] args) {
//
//		//zip压缩源文件
//		String zipName = "CUSED.KLFG.20191203.00.U.ZIP";
//		String zipPath = "E:/temp/zip/" + zipName;
//		String sourcePath = "E:/temp/source/CUSED.KLFG.20191203.00.U";
//		ZipUtils.fileToZip(sourcePath, zipPath);
//
//		//pgp加密
//		String pgpName = String.format("%s.DAT",zipName);
//		String pgpPath = "E:/temp/pgp/" + pgpName;
//		try {
//			PgpEncryUtil.Encry(zipPath, pgpPath, "E:/temp/dsfpublic.asc");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
}
