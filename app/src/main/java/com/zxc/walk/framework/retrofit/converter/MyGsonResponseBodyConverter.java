package com.zxc.walk.framework.retrofit.converter;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.zxc.walk.framework.retrofit.constants.ResultCode;
import com.zxc.walk.framework.retrofit.entity.Result;
import com.zxc.walk.framework.retrofit.exception.ServerException;
import com.zxc.walk.framework.utils.GsonUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * @author xyu
 * @date 2019/1/29
 * Describe:
 */
public class MyGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;
    private static final Charset UTF_8 = Charset.forName("UTF-8");

    public MyGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String resp = value.string();

        Result response = GsonUtils.fromJson(resp, Result.class);
        if (null == response || null == response.getCode()) {
            throw new RuntimeException("null == response || null == data");
        }
        if (!ResultCode.CODE_SUCCESS.equals(response.getInnercode())) {//佛了，一会code9999,一会innercode9999
            throw new ServerException(response.getInnercode(), response.getMessage().toString());
        } else if (!ResultCode.CODE_SUCCESS.equals(response.getCode())) {
            throw new ServerException(response.getCode(), response.getMessage().toString());
        }

        MediaType contentType = value.contentType();
        Charset charset = contentType != null ? contentType.charset(UTF_8) : UTF_8;
        InputStream inputStream = new ByteArrayInputStream(resp.getBytes());
        Reader reader = new InputStreamReader(inputStream, charset);
        JsonReader jsonReader = gson.newJsonReader(reader);
        try {
            T result = adapter.read(jsonReader);
            if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
                throw new JsonIOException("JSON document was not fully consumed.");
            }
            return result;
        } finally {
            value.close();
        }
    }
}
