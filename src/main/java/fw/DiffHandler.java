package fw;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.io.InputStream;

public class DiffHandler implements RequestHandler<DiffHandler.RequestClass, DiffHandler.ResponseClass> {

    @Override
    public ResponseClass handleRequest(RequestClass input, Context context) {
        ResponseClass responseClass = new ResponseClass();
        try {
            responseClass.result = diff(input.source, input.destination);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseClass;
    }

    public static class RequestClass {
        public String source;
        public String destination;
    }


    public static class ResponseClass {
        public String result;
    }


    public String diff(InputStream source, InputStream destination) {
        return "";
    }


    public String diff(String source, String destination) {
        return source + destination;
    }
}
