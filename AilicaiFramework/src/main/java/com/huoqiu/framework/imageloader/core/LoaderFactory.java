package com.huoqiu.framework.imageloader.core;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * class name:LoaderFactory <BR>
 * class description: 图片加载器工厂类<BR>
 * Remark: <BR>
 *
 * @author IWJW)ZhouXuan
 * @version 1.00 2015-7-13
 */
public class LoaderFactory {

    public static Map<Class, ImageDisplayer> map = new LinkedHashMap<Class, ImageDisplayer>();

    public static ImageDisplayer getLoader(Class<? extends ImageDisplayer> cls)
            throws InstantiationException, IllegalAccessException {
        ImageDisplayer loader = map.get(cls);
            synchronized (LoaderFactory.class) {
                if (loader == null) {
                    register(cls.newInstance());
                }
            }
        return map.get(cls);
    }

    private static void register(ImageDisplayer loder) {
        map.put(loder.getClass(), loder);
    }

    public static Collection<ImageDisplayer> getAll() {
        return map.values();
    }
}
