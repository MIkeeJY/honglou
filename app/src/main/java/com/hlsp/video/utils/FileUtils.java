package com.hlsp.video.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * 支持读取各种文件
 */
public class FileUtils {

    /**
     * 删除文件
     *
     * @param context  程序上下文
     * @param fileName 文件名，要在系统内保持唯一,并且要是在file文件夹里面的文件才能正确删除
     * @return boolean 存储成功的标志
     */
    public static boolean deleteFile(Context context, String fileName) {
        return context.deleteFile(fileName);
    }

    /**
     * @param fileName 全路径名字
     * @return
     */
    public static boolean deleteFile(String fileName) {
        try {
            File file = new File(fileName);
            if (file.exists()) {
                return file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 文件是否存在
     *
     * @param context
     * @param fileName
     * @return
     */
    public static boolean exists(Context context, String fileName) {
        return new File(context.getFilesDir(), fileName).exists();
    }

    /**
     * 存储文本数据
     *
     * @param context  程序上下文
     * @param fileName 文件名，要在系统内保持唯一
     * @param content  文本内容
     * @return boolean 存储成功的标志
     */
    public static boolean writeFile(Context context, String fileName, String content) {
        boolean success = false;
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            byte[] byteContent = content.getBytes();
            fos.write(byteContent);

            success = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }

        return success;
    }

    /**
     * 存储文本数据
     *
     * @param filePath 文件全路径
     * @param content  文本内容
     * @return boolean 存储成功的标志
     */
    public static boolean writeFile(String filePath, String content) {
        boolean success = false;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filePath);
            byte[] byteContent = content.getBytes();
            fos.write(byteContent);

            success = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }

        return success;
    }

    /**
     * 读取文本数据
     *
     * @param context  程序上下文
     * @param fileName 文件名
     * @return String, 读取到的文本内容，失败返回null
     */
    public static String readFile(Context context, String fileName) {
        if (!exists(context, fileName)) {
            return null;
        }
        FileInputStream fis = null;
        String content = null;
        try {
            fis = context.openFileInput(fileName);
            if (fis != null) {

                byte[] buffer = new byte[1024];
                ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
                while (true) {
                    int readLength = fis.read(buffer);
                    if (readLength == -1)
                        break;
                    arrayOutputStream.write(buffer, 0, readLength);
                }
                fis.close();
                arrayOutputStream.close();
                content = new String(arrayOutputStream.toByteArray());
                if (content.contains("null")) {//当服务器返回的数据为null时，写入到文件中的值为null,下一次到这里会取出空值
                    content = null;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            content = null;
        } finally {
            try {
                if (fis != null)
                    fis.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        return content;
    }

    /**
     * 读取文本数据
     *
     * @param filePath 文件全路径
     * @return String, 读取到的文本内容，失败返回null
     */
    public static String readFile(String filePath) {
        if (filePath == null || !new File(filePath).exists()) {
            return null;
        }
        FileInputStream fis = null;
        String content = null;
        try {
            fis = new FileInputStream(filePath);
            if (fis != null) {

                byte[] buffer = new byte[1024];
                ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
                while (true) {
                    int readLength = fis.read(buffer);
                    if (readLength == -1)
                        break;
                    arrayOutputStream.write(buffer, 0, readLength);
                }
                fis.close();
                arrayOutputStream.close();
                content = new String(arrayOutputStream.toByteArray());

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            content = null;
        } finally {
            try {
                if (fis != null)
                    fis.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        return content;
    }

    /**
     * @param rawResource raw目录下面的文件
     * @return
     * @throws IOException
     */
    public static String readRawFile(Context context, int rawResource) throws IOException {
        InputStream in = context.getResources().openRawResource(rawResource);
        int length = in.available();
        byte[] buffer = new byte[length];
        in.read(buffer);
        return new String(buffer, "UTF-8");
    }

    /**
     * 读取文本数据
     *
     * @param context  程序上下文
     * @param fileName 文件名
     * @return String, 读取到的文本内容，失败返回null
     */
    public static String readAssets(Context context, String fileName) {
        InputStream is = null;
        String content = null;
        try {
            is = context.getAssets().open(fileName);
            if (is != null) {

                byte[] buffer = new byte[1024];
                ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
                while (true) {
                    int readLength = is.read(buffer);
                    if (readLength == -1)
                        break;
                    arrayOutputStream.write(buffer, 0, readLength);
                }
                is.close();
                arrayOutputStream.close();
                content = new String(arrayOutputStream.toByteArray());

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            content = null;
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        return content;
    }

    /**
     * 存储单个Parcelable对象
     *
     * @param context      程序上下文
     * @param fileName     文件名，要在系统内保持唯一
     * @param parcelObject 对象必须实现Parcelable
     * @return boolean 存储成功的标志
     */
    @Deprecated
    public static <T extends Parcelable> boolean writeParcelable(Context context, String fileName, T parcelObject) {
        boolean success = false;
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            Parcel parcel = Parcel.obtain();
            parcel.writeParcelable(parcelObject, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
            byte[] data = parcel.marshall();
            fos.write(data);

            success = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }

        return success;
    }


    /**
     * 存储List对象
     *
     * @param context  程序上下文
     * @param fileName 文件名，要在系统内保持唯一
     * @param list     对象数组集合，对象必须实现Parcelable
     * @return boolean 存储成功的标志
     */
    @Deprecated
    public static <T extends Parcelable> boolean writeParcelableList(Context context, String fileName, List<T> list) {
        boolean success = false;
        FileOutputStream fos = null;
        try {
            if (list instanceof List) {
                fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
                Parcel parcel = Parcel.obtain();
                parcel.writeList(list);
                byte[] data = parcel.marshall();
                fos.write(data);

                success = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }

        return success;
    }

    /**
     * 读取单个数据对象
     *
     * @param context  程序上下文
     * @param fileName 文件名
     * @return 读取到的Parcelable对象，失败返回null
     */
    @Deprecated
    public static <T extends Parcelable> T readParcelable(Context context, String fileName, Class<T> classOfT) {
        T parcelObject = null;
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        try {
            fis = context.openFileInput(fileName);
            if (fis != null) {
                bos = new ByteArrayOutputStream();
                byte[] b = new byte[4096];
                int bytesRead;
                while ((bytesRead = fis.read(b)) != -1) {
                    bos.write(b, 0, bytesRead);
                }

                byte[] data = bos.toByteArray();

                Parcel parcel = Parcel.obtain();
                parcel.unmarshall(data, 0, data.length);
                parcel.setDataPosition(0);
                parcelObject = parcel.readParcelable(classOfT.getClassLoader());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            parcelObject = null;
        } finally {
            if (fis != null)
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (bos != null)
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        return parcelObject;
    }

    /**
     * 读取数据对象列表
     *
     * @param context  程序上下文
     * @param fileName 文件名
     * @return List, 读取到的对象数组，失败返回null
     */

    @Deprecated
    public static <T extends Parcelable> List<T> readParcelableList(Context context, String fileName, Class<T> classOfT) {
        List<T> results = null;
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        try {
            File file = new File(context.getFilesDir() + File.separator + fileName);
            if (!file.exists())
                return null;
            fis = context.openFileInput(fileName);
            if (fis != null) {
                bos = new ByteArrayOutputStream();
                byte[] b = new byte[4096];
                int bytesRead;
                while ((bytesRead = fis.read(b)) != -1) {
                    bos.write(b, 0, bytesRead);
                }

                byte[] data = bos.toByteArray();

                Parcel parcel = Parcel.obtain();
                parcel.unmarshall(data, 0, data.length);
                parcel.setDataPosition(0);
                results = parcel.readArrayList(classOfT.getClassLoader());
            }
        } catch (Exception e) {
            e.printStackTrace();
            results = null;
        } finally {
            if (fis != null)
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (bos != null)
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        return results;
    }

    /**
     * 读取数据对象
     *
     * @param fileName 文件名
     * @param classOfT 对象类别，例如Order.class
     * @return T, 读取到的POJO对象，失败返回null
     */
    public static <T> T readObjectFromJsonFile(Context context, String fileName, Class<T> classOfT) {
        T object = null;
        String content = readFile(context, fileName);
        if (content != null && content.trim().length() > 0) {
            object = new Gson().fromJson(content, classOfT);
        }

        return object;
    }

    /**
     * 读取数据对象
     *
     * @param fileName 文件名
     * @param classOfT 对象类别，例如Order.class
     * @return T, 读取到的POJO对象，失败返回null
     */
    public static <T> List<T> readListFromJsonFile(Context context, String fileName, Class<T> classOfT) {
        List<T> object = null;
        String content = readFile(context, fileName);
        if (content != null && content.trim().length() > 0) {
            object = listFromJson(content, classOfT);
        }
        return object;
    }

    public static <T> List<T> listFromJson(String json, Class<T> classOfT) {
        return new Gson().fromJson(json, new GenericType(classOfT));
    }

    private static class GenericType implements ParameterizedType {
        private Class itemClazz;

        public GenericType(Class itemClazz) {
            this.itemClazz = itemClazz;
        }

        public Type[] getActualTypeArguments() {
            return new Type[]{itemClazz};
        }

        public Type getRawType() {
            return ArrayList.class;
        }

        public Type getOwnerType() {
            return null;
        }
    }

    /**
     * 存储文本数据，一般用作网络数据的硬盘缓存
     *
     * @param fileName 文件名，要在系统内保持唯一
     * @param list     数组对象，包含任意的POJO对象
     * @return boolean 存储成功的标志
     */
    public static boolean writeListToJsonFile(Context context, String fileName, List list) {
        return writeFile(context, fileName, new Gson().toJson(list));
    }

    /**
     * 存储文本数据，一般用作网络数据的硬盘缓存
     *
     * @param fileName 文件名，要在系统内保持唯一
     * @param object   任意POJO对象
     * @return boolean 存储成功的标志
     */
    public static boolean writeObjectToJsonFile(Context context, String fileName, Object object) {
        return writeFile(context, fileName, new Gson().toJson(object));
    }

    public static boolean saveSerializable(Context context, String fileName, Serializable data) {
        boolean success = false;
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(context.openFileOutput(fileName, Context.MODE_PRIVATE));
            oos.writeObject(data);
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return success;
    }

    public static Serializable readSerialLizable(Context context, String fileName) {
        Serializable data = null;
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(context.openFileInput(fileName));
            data = (Serializable) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return data;
    }

    /**
     * 从assets里边读取字符串
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String getFromAssets(Context context, String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 复制文件
     *
     * @param srcFile
     * @param dstFile
     * @return
     */
    public static boolean copy(String srcFile, String dstFile) {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {

            File dst = new File(dstFile);
            if (!dst.getParentFile().exists()) {
                dst.getParentFile().mkdirs();
            }

            fis = new FileInputStream(srcFile);
            fos = new FileOutputStream(dstFile);

            byte[] buffer = new byte[1024];
            int len = 0;

            while ((len = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return true;
    }

    /**
     * 解压zip文件
     *
     * @param zipPath    zip原目录
     * @param outputPath zip输出目录
     */
    public static void parseZip(String zipPath, String outputPath) {
        try {
            ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipPath));
            BufferedInputStream bufferedInputStream = new BufferedInputStream(zipInputStream);
            File file;
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null && !entry.isDirectory()) {
                file = new File(outputPath, entry.getName());
                if (!file.exists()) {
                    (new File(file.getParent())).mkdirs();
                }
                FileOutputStream out = new FileOutputStream(file);
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(out);
                int b;
                while ((b = bufferedInputStream.read()) != -1) {
                    bufferedOutputStream.write(b);
                }
                bufferedOutputStream.close();
                out.close();
            }
            bufferedInputStream.close();
            zipInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 递归删除目录中的子目录下
     *
     * @param dir 要删除的目录
     * @return 是否删除成功
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            if (null != children && children.length > 0) {
                for (int i = 0; i < children.length; i++) {
                    boolean success = deleteDir(new File(dir, children[i]));
                    if (!success) {
                        return false;
                    }
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }


    /**
     * 获得指定文件的byte数组
     */
    public static byte[] getBytes(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    /**
     * 根据byte数组，生成文件
     */
    public static void getFile(byte[] bfile, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {//判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath + "\\" + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 获得指定图片文件的byte数组
     */
    public static String convertIconToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] appicon = baos.toByteArray();// 转为byte数组
        return Base64.encodeToString(appicon, Base64.DEFAULT);
    }

}
