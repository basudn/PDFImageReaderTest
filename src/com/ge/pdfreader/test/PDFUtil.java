package com.ge.pdfreader.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class PDFUtil {

	public static void main(String[] args) {
		try {
			PDDocument document = PDDocument.load(new File(
					"C:\\Users\\Debayan Basu\\Downloads\\Shop Visit Reports-selected\\01 Ride-on pack of 906381 R01.PDF"));
			Tesseract tesseract = new Tesseract();
			tesseract.setLanguage("eng");
			tesseract.setDatapath(System.getenv("TESSDATA_PREFIX"));
			for (PDPage page : document.getPages()) {
				List<BufferedImage> images = getImagesFromResources(page.getResources());
				for (BufferedImage bufferedImage : images) {
					System.out.println(tesseract.doOCR(bufferedImage));
				}
			}
		} catch (IOException | TesseractException e) {
			System.out.println("Some error: " + e.getMessage());
		}
	}

	private static List<BufferedImage> getImagesFromResources(PDResources resources) throws IOException {
		List<BufferedImage> images = new ArrayList<BufferedImage>();

		for (COSName xObjectName : resources.getXObjectNames()) {
			PDXObject xObject = resources.getXObject(xObjectName);

			if (xObject instanceof PDFormXObject) {
				images.addAll(getImagesFromResources(((PDFormXObject) xObject).getResources()));
			} else if (xObject instanceof PDImageXObject) {
				images.add(((PDImageXObject) xObject).getImage());
			}
		}

		return images;
	}

}
