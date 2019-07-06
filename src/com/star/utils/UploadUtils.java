package com.star.utils;

import java.util.UUID;

/**
 * �ļ��ϴ��Ĺ�����
 * @author star
 *
 */
public class UploadUtils {

	/**
	 * ����Ψһ���ļ���
	 */
	public static String getUUIDFileName(String fileName) {
		//���ļ���ǰ�沿�ֽ��н�ȡ��xx.jpg ---> .jpg
		int idx = fileName.lastIndexOf(".");
		String extention = fileName.substring(idx);   //�õ���׺��
		String uuidFileName = UUID.randomUUID().toString().replace("-", "")+extention;
		return uuidFileName;
	}
}
