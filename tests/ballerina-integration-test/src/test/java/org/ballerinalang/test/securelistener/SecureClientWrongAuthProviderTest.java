/*
*  Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*  http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing,
*  software distributed under the License is distributed on an
*  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*  KIND, either express or implied.  See the License for the
*  specific language governing permissions and limitations
*  under the License.
*/

package org.ballerinalang.test.securelistener;

import io.netty.handler.codec.http.HttpHeaderNames;
import org.ballerinalang.test.IntegrationTestCase;
import org.ballerinalang.test.context.ServerInstance;
import org.ballerinalang.test.util.HttpClientRequest;
import org.ballerinalang.test.util.HttpResponse;
import org.ballerinalang.test.util.TestConstant;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Test cases for verifying wrong auth provider for a service.
 */
public class SecureClientWrongAuthProviderTest extends IntegrationTestCase {

    private ServerInstance ballerinaServer;

    @BeforeClass
    public void setup() throws Exception {
        String basePath = new File(
                "src" + File.separator + "test" + File.separator + "resources" + File.separator + "secureListener")
                .getAbsolutePath();
        String balFilePath = basePath + File.separator + "secure-listener-wrong-provider-id-test.bal";
        String ballerinaConfPath = basePath + File.separator + "ballerina.conf";
        startServer(balFilePath, ballerinaConfPath);
    }

    private void startServer(String balFile, String configPath) throws Exception {
        ballerinaServer = ServerInstance.initBallerinaServer();
        ballerinaServer.startBallerinaServerWithConfigPath(balFile, configPath);
    }

    @Test(description = "Authn failure with wrong auth provider")
    public void testAuthSuccess() throws Exception {
        Map<String, String> headers = new HashMap<>();
        headers.put(HttpHeaderNames.CONTENT_TYPE.toString(), TestConstant.CONTENT_TYPE_TEXT_PLAIN);
        headers.put("Authorization", "Basic aXN1cnU6eHh4");
        HttpResponse response = HttpClientRequest.doGet(ballerinaServer.getServiceURLHttp("echo/test"), headers);
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getResponseCode(), 401, "Response code mismatched");
    }

    @AfterClass public void tearDown() throws Exception {
        ballerinaServer.stopServer();
    }

}
