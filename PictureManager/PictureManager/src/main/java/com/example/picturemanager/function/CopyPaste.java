package com.example.picturemanager.function;

import java.io.*;

public class CopyPaste
{

    private String Source;// 要复制的文件路径
    private String Target;// 文件目标路径

    public CopyPaste(String Source)
    {
        this.Source=Source;
    }

    public CopyPaste(String Source, String Target)
    {
        this.Source=Source;
        this.Target=Target;
    }


    public String getSource()
    {
        return Source;
    }

    public String getTarget()
    {
        return Target;
    }

    public void setSource(String source)
    {
        Source = source;
    }

    public void setTarget(String target)
    {
        Target = target;
    }

    public void PasteFile()
    {
        InputStream is; // 创建字节输入流
        OutputStream os; // 创建字节输出流
        BufferedInputStream bis = null;// 创建缓冲输入字节流
        BufferedOutputStream bos = null;// 创建缓冲字节输出流
        try
        {
            is = new FileInputStream(Source);// 创建文件输入流
            os = new FileOutputStream(Target);// 创建文件输出流，指向目标路径
            bis = new BufferedInputStream(is);// 用缓冲输入流装饰文件输入流
            bos = new BufferedOutputStream(os);// 用缓冲输出流来包装文件输出流
            int size = 0;
            while ((size = bis.read()) != -1)
            {
                bos.write(size);// 对接流，输出文件
            }

        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                bos.close();
                bis.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

        }
    }

    public void PasteTargetIsExisted()
    {
        while(new File(Target).exists())
        {
            setTarget(Target.substring(0,Target.lastIndexOf("."))+"(1)"+Target.substring(Target.lastIndexOf(".")));
        }
    }

}
