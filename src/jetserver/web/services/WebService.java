

package jetserver.web.services;

import java.io.IOException;

import jetserver.web.*;

public interface WebService {

    void service(HttpRequest request, HttpResponse response) throws IOException;
}
