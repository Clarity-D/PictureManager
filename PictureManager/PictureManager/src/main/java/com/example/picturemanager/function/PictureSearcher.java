package com.example.picturemanager.function;

import com.example.picturemanager.tree.Tree;

import java.io.File;

//显示计算机目录及里面的所有照片.jpg、.jpeg、gif、.png、.bmp,统计照片数量和总大小

public class PictureSearcher
{
    private File target;

    public PictureSearcher(String path)
    {
        target=new File(path);
    }

    public File GetFile()
    {
        return target.getAbsoluteFile();
    }

    public String GetFileName()
    {
        return target.getName();
    }

    public String GetFileWholeName()
    {
        return target.getAbsolutePath();
    }

    public boolean IsExists()
    {
        return target.exists();
    }

    public boolean IsFile()
    {
        return target.isFile();
    }


    public long GetBitOfFile()
    {
        return target.length();
    }

    public String GetPath()
    {
        return target.getPath();
    }

    public boolean IsDirectory()
    {
        return target.isDirectory();
    }

    public String GetParent()
    {
        return target.getParent();
    }

    public Tree MenuSearcher(Tree parents)//每点开一层，就显示该层所有目录版本
    {
        Tree node=new Tree(GetFileName());
        node.SetParents(parents);
        if(IsFile())//如果该目标是文件而不是目录
        {
            Tree nul=new Tree("null");
            return nul;
        }
        File[] tmp=GetFile().listFiles();
        if(tmp==null)
        {
            Tree nul=new Tree("null");
            return nul;
        }
        for(int i=0;i<tmp.length;i++)//递归调用该函数以获得子节点
        {
            if(tmp[i].isFile())//排除非目录
            {
                continue;
            }
            PictureSearcher tmp2=new PictureSearcher(tmp[i].getAbsolutePath());
            Tree newChild=new Tree(tmp2.GetFileName());
            newChild.setWholeName(tmp2.GetFileWholeName());
            node.AddChildren(newChild);//设置子节点
            node.AddNumOfChildren();
        }
        return node;

    }

    public Tree PictureCounter(Tree parents)//查找该层的图片
    {
        double allSize=0;
        Tree node=new Tree(GetFileName());
        node.SetParents(parents);
        File[] tmp=GetFile().listFiles();
        if(tmp==null)
        {
            Tree nul=new Tree("null");
            return nul;
        }
        for(int i=0;i<tmp.length;i++)
        {
            if(tmp[i].isFile())//如果该目标是文件而不是目录
            {
                String fileName = tmp[i].getName();
                String type = fileName.substring(fileName.lastIndexOf(".") + 1);
                //.jpg、.jpeg、gif、.png、.bmp
                if (type.equals("jpg") || type.equals("jpeg") || type.equals("gif") || type.equals("png") || type.equals("bmp")||
                type.equals("JPG") || type.equals("JPEG") || type.equals("GIF") || type.equals("PNG") || type.equals("BMP"))
                {
                    allSize+=tmp[i].length();
                    Tree tmps=new Tree(fileName);
                    tmps.setWholeName(tmp[i].getAbsolutePath());
                    node.AddChildren(tmps);
                    node.AddNumOfChildren();
                }
                else
                {
                    continue;
                }

            }

        }
        node.setAllSize(allSize);
        return node;


    }

    /*public Tree PictureCounter(Tree parents)//递归到底版本
    {
        Tree node=new Tree(GetFileName());
        node.SetParents(parents);
        if(IsFile())//如果该目标是文件而不是目录
        {
            String fileName =GetFileName();
            String type = fileName.substring(fileName.lastIndexOf(".") + 1);
            //.jpg、.jpeg、gif、.png、.bmp
            if(type.equals("jpg") || type.equals("jpeg") || type.equals("gif") || type.equals("png") || type.equals("bmp"))
            {
                if(!node.GetIsPictureExist())
                {
                    node.TrueIsPictureExist();
                    node.GetParents().TrueIsPictureExist();
                }
                return node;
            }
            else
            {
                Tree nullfile=new Tree("null");
                nullfile.SetNumOfChildren(-1);
                return nullfile;
            }
        }
        File[] tmp=GetFile().listFiles();
        if(tmp==null)
        {
            Tree nullfile=new Tree("null");
            nullfile.SetNumOfChildren(-1);
            return nullfile;
        }
        for(int i=0;i<tmp.length;i++)//递归调用该函数以获得子节点
        {
            PictureSearcher tmp2=new PictureSearcher(tmp[i].getAbsolutePath());
            Tree newChild=new Tree(tmp2.GetFileName());
            Tree tmps=tmp2.PictureCounter(node);//check
            if(tmps.GetNumOfChildren()==-1)
            {
                continue;
            }
            if(!tmps.GetIsPictureExist())
            {
                continue;
            }
            node.GetParents().TrueIsPictureExist();
            node.AddChildren(tmps);//total=tmp2.GetBitOfDirectory()+total;设置子节点
            node.AddNumOfChildren();

        }
        return node;

    }*/

}
