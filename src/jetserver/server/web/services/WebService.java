

package jetserver.server.web.services;

import java.io.IOException;

import jetserver.server.web.*;

public interface WebService {

    void service(HttpRequest request, HttpResponse response) throws IOException;
}
