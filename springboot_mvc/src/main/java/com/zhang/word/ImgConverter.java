package com.zhang.word;

import net.arnx.wmf2svg.gdi.svg.SvgGdi;
import net.arnx.wmf2svg.gdi.svg.SvgGdiException;
import net.arnx.wmf2svg.gdi.wmf.WmfParseException;
import net.arnx.wmf2svg.gdi.wmf.WmfParser;
import org.apache.commons.lang3.StringUtils;
import org.freehep.graphicsio.emf.EMFInputStream;
import org.freehep.graphicsio.emf.EMFRenderer;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;
import org.w3c.dom.Document;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.zip.GZIPOutputStream;

public class ImgConverter {

	public static void main(String[] args) {
		try {
			//String result = latex2Png("f\\left(x\\right)");
			//System.out.println(result);
			//wmfToSvg("E:/imges/image9.wmf", "E:/imges/image9.svg");
			String wmfStr = "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";
			String svgStr = wmfConversionSvg(wmfStr);
			System.out.println(svgStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * wmf转换为png图片格式
	 * @param wmfBase64Str
	 * @return
	 * @throws IOException
	 * @throws SvgGdiException
	 * @throws Exception
	 */
	public static String wmfConversionSvg(String wmfBase64Str) throws IOException, SvgGdiException{

		byte[] data = decoder.decodeBuffer(wmfBase64Str);
		final SvgGdi gdi = new SvgGdi(false);
    	ByteArrayInputStream is = new ByteArrayInputStream(data);
        WmfParser parser = new WmfParser();
        String svgBase64Str = "";
        try {
			parser.parse(is, gdi);
			Document doc = gdi.getDocument();
			ByteArrayOutputStream svgByteArrayOutputStream = new ByteArrayOutputStream();
			output(doc, svgByteArrayOutputStream);
			byte[] svgData = svgByteArrayOutputStream.toByteArray();
			/*String str = new String(svgData);
			svgBase64Str = str.replace("\"", "'");
			//解决操作系统编码非utf-8的情况
			svgBase64Str = new String(svgBase64Str.getBytes(System.getProperty("file.encoding").toUpperCase()) , "UTF-8");*/
			String str = encoder.encode(svgData);
			svgBase64Str = new String(str.getBytes(System.getProperty("file.encoding").toUpperCase()) , "UTF-8");
		} catch (WmfParseException e) {
			e.printStackTrace();
		}

        //关闭流
        if(is != null) {
        	is.close();
        }
		return svgBase64Str;
	}

	public static void wmfToSvg(String src, String dest) {
        File file = new File(src);
        boolean compatible = false;
        try {
            InputStream in = new FileInputStream(file);
            WmfParser parser = new WmfParser();
            final SvgGdi gdi = new SvgGdi(compatible);
            parser.parse(in, gdi);

            Document doc = gdi.getDocument();
            OutputStream out = new FileOutputStream(dest);
            if (dest.endsWith(".svgz")) {
                out = new GZIPOutputStream(out);
            }

            output(doc, out);
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

	private static void output(Document doc, OutputStream out) throws IOException{
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer;
		try {
			transformer = factory.newTransformer();
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC,
					"-//W3C//DTD SVG 1.0//EN");
			transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,
					"http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd");
			transformer.transform(new DOMSource(doc), new StreamResult(out));


		} catch (TransformerException e) {
			e.printStackTrace();
		}
		if(out!=null) {
			out.flush();
			out.close();
		}

    }






	 public static sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
	 public static sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();


	 /**
     * emf转换为png图片格式
     *
     * @param
     * @return
     * @throws IOException
     */
    public static String emfConversionPng(String emfBase64Str) throws IOException {
    	byte[] data = decoder.decodeBuffer(emfBase64Str);
    	ByteArrayInputStream is = new ByteArrayInputStream(data);
        EMFInputStream eis = new EMFInputStream(is,
                EMFInputStream.DEFAULT_VERSION);
        EMFRenderer emfRenderer = new EMFRenderer(eis);
        final int width = (int) eis.readHeader().getBounds()
                .getWidth();
        final int height = (int) eis.readHeader().getBounds()
                .getHeight();
        // 设置图片的大小和样式
        final BufferedImage result = new BufferedImage(width + 60,
                height + 40, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2 = result.createGraphics();
        emfRenderer.paint(g2);

        //result.

        // 写入到磁盘中(格式设置为png背景不会变为橙色)
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(result,"png",os);
        byte[] pngData =  os.toByteArray();

        String pngBase64 = encoder.encode(pngData);

        os.close();
        eis.close();
        is.close();

        return pngBase64;
    }

    public static String latex2Png(String latex) {
		try {
			TeXFormula formula = new TeXFormula(latex);
			TeXIcon icon = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 20);
			icon.setInsets(new Insets(5, 5, 5, 5));
			BufferedImage image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_BYTE_GRAY);

			Graphics2D g2 = image.createGraphics();
			g2.setColor(Color.white);
			g2.fillRect(0, 0, icon.getIconWidth(), icon.getIconHeight());
			JLabel jl = new JLabel();
			jl.setForeground(new Color(0, 0, 0));
			icon.paintIcon(jl, g2, 0, 0);

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			try {
				ImageIO.write(image, "png", outputStream);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
			byte[] buffer = outputStream.toByteArray();
			sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
			return ( encoder.encode(buffer));
		} catch (Exception e) {
			System.err.println("公式解析有误：\n" + latex);
			return null;
		}

	}

	public static void base64StrToImage(String imgStr, String fileName) {
		try {
			byte[] bytes1 = decoder.decodeBuffer(imgStr);
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes1);
			BufferedImage bi1 = ImageIO.read(bais);
			File f1 = new File(fileName);
			ImageIO.write(bi1, "jpg", f1);


			FileOutputStream fos = new FileOutputStream(fileName);
			fos.write(bytes1);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String[] FILETYPES = new String[]{
			".jpg", ".bmp", ".jpeg", ".png", ".gif"
	};

	public static boolean isImgSuffix(String suffix){
		if(StringUtils.isBlank(suffix)){
			return false;
		}
		for(String type:FILETYPES){
			suffix = suffix.toLowerCase();
			if(type.equals(suffix)){
				return true;
			}
		}
		return false;
	}
}
