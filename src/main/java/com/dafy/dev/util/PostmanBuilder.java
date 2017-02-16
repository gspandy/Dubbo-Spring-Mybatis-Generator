package com.dafy.dev.util;

import com.dafy.dev.pojo.PostmanModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.UUID;

/**
 * Created by m000665 on 2017/2/15.
 */
public class PostmanBuilder {
    ThreadLocal<PostmanModel> postmanModelThreadLocal = new ThreadLocal<>();

    public static PostmanBuilder builder(String name) {

        PostmanModel postmanModel = new PostmanModel();

        postmanModel.name = name;
        postmanModel.description = "";

        postmanModel.order = new LinkedList<>();
        postmanModel.folders = new LinkedList<>();
        postmanModel.timestamp = System.currentTimeMillis();

        postmanModel.requests = new LinkedList<>();
        postmanModel.id = UUID.randomUUID().toString();

        PostmanBuilder postmanBuilder = new PostmanBuilder();
        postmanBuilder.postmanModelThreadLocal.set(postmanModel);

        return postmanBuilder;
    }

    private PostmanBuilder addFolder(String name) {

        PostmanModel postmanModel = postmanModelThreadLocal.get();

        PostmanModel.Folder folder = new PostmanModel.Folder();
        folder.name = name;
        folder.id = UUID.randomUUID().toString();
        folder.order = new LinkedList<>();
        folder.owner = 0;
        folder.collectionId = postmanModel.id;

        postmanModel.folders.add(folder);

        return this;
    }

    public PostmanBuilder addRequest(String folder, String name, String url, String method,
                                     Map<String, String> headers, String body, boolean format) {

        PostmanModel postmanModel = postmanModelThreadLocal.get();

        PostmanModel.Request request = new PostmanModel.Request();
        request.id = UUID.randomUUID().toString();
        request.url = url;
        request.preRequestScript = null;
        request.pathVariables = new HashMap();
        request.method = method;
        request.data = new LinkedList();
        request.dataMode = "raw";
        request.tests = null;
        request.currentHelper = "normal";
        request.helperAttributes = new HashMap();
        request.time = System.currentTimeMillis();
        request.name = name;
        request.description = "";
        request.responses = new LinkedList();

        request.collectionId = postmanModel.id;
        request.headers = buildHeaders(headers);

        request.rawModeData = format ? format(body.toCharArray()) : body;

        postmanModel.requests.add(request);
        if (folder!=null) {
            findFolder(this, folder).order.add(request.id);
        }
        return this;
    }

    private static String format(char[] str) {
        int tab = 0;
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < str.length; i++) {
            switch (str[i]) {
                case '[':
                case '{':
                    stringBuffer.append('\n');
                    for (int j = 0; j < tab; j++) {
                        stringBuffer.append('\t');
                    }
                    stringBuffer.append(str[i]);
                    stringBuffer.append('\n');
                    tab++;
                    for (int j = 0; j < tab; j++) {
                        stringBuffer.append('\t');
                    }
                    break;
                case ',':
                    stringBuffer.append(str[i]);
                    stringBuffer.append('\n');
                    for (int j = 0; j < tab; j++) {
                        stringBuffer.append('\t');
                    }
                    break;
                case '}':
                case ']':
                    stringBuffer.append('\n');
                    tab--;
                    for (int j = 0; j < tab; j++) {
                        stringBuffer.append('\t');
                    }
                    stringBuffer.append(str[i]);
                    break;
                default:
                    stringBuffer.append(str[i]);
            }

        }
        return stringBuffer.toString();
    }

    public PostmanModel build() {
        try {
            return postmanModelThreadLocal.get();
        } finally {
            release();
        }
    }

    public void out(OutputStream outputStream) {
        PostmanModel postmanModel = postmanModelThreadLocal.get();
        ObjectMapper objectMapper = new ObjectMapper();
        BufferedOutputStream bufferedOutputStream = null;
        try {
            bufferedOutputStream = new BufferedOutputStream(outputStream);

            bufferedOutputStream.write(objectMapper.writeValueAsBytes(postmanModel));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        release();
    }

    public PostmanBuilder release() {
        postmanModelThreadLocal.remove();
        return this;
    }

    private static PostmanModel.Folder findFolder(PostmanBuilder builder, String name) {

        List<PostmanModel.Folder> list = builder.postmanModelThreadLocal.get().folders;

        for (ListIterator<PostmanModel.Folder> it = list.listIterator(list.size()); it
                .hasPrevious();) {
            PostmanModel.Folder cur = it.previous();
            if (name.equals(cur.name)) {
                return cur;
            }
        }
        builder.addFolder(name);
        return findFolder(builder, name);
    }

    private static String buildHeaders(Map<String, String> headers) {
        StringBuffer stringBuffer = new StringBuffer();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            stringBuffer.append(entry.getKey()).append(":").append(entry.getValue()).append("\n");
        }
        return stringBuffer.toString();
    }
}
