package com.weibo.util;


import java.io.IOException;
import java.net.MalformedURLException;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
/**
 * UNUSED!.
 */
public class HtmlUnitClient {
    public static void main(String[] args) throws FailingHttpStatusCodeException, MalformedURLException, IOException, ClassNotFoundException {

        final WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setTimeout(50000);
        webClient.getCookieManager().setCookiesEnabled(true);
        String[] cookieList = "YF-Ugrow-G0=1eba44dbebf62c27ae66e16d40e02964; SUS=SID-2389429510-1404504056-GZ-w5xsd-118f6ff932f4a1d8761e47a46e926317; TC-Page-G0=5e366de3f93a843287cf57cc85cd7f80; WBtopGlobal_register_version=3c1867588e99a11e; SUBP=002A2c-gVlwEm1uAWxfgXELuuu1xVxBxALVOri3lKWgx2RHO0sZr5r-uHY-u_1%3D; un=jtiger@qq.com; SUE=es%3D8c09157257591b120f489fe371046387%26ev%3Dv1%26es2%3D0191af223c443e2dba18ea3ad83a28ef%26rs0%3DyemBugSM%252BVV9ZLIH2LNQ0BqKpIeBJmiK0YA9Yda%252Bzrxl1V8YO8uLh80W%252FNW4y8gESceGEpj81sb7ZAO%252FMGftCi4HpFSrF%252Ba%252BKUo2H%252BlCf6zwisjZecZm1n0hEPCXpo0CbVvKyM23sM5CcjeYL3tjQIJaf%252BSlWYHUu28Mmny64V8%253D%26rv%3D0; Apache=7312597868391.589.1404503958021; SINAGLOBAL=7312597868391.589.1404503958021; ULV=1404503958025:1:1:1:7312597868391.589.1404503958021:; SUB=AUwRLTcgvqF%2B%2BQmp82Ua%2FJz9fyY43MRaDW0xgywTJdzk7CBhs93yOnOb1%2FhKwcgbrQCz7L0tp24S2IKpG0AEwhxewwVuC7KGVCTi3Bb4LyK6wdjoY6v713qJWsc13v3Pg4qe9gXU1Bn%2BIi3qKA46jLM%3D; ALF=1436040056; YF-Page-G0=f017d20b1081f0a1606831bba19e407b; SSOLoginState=1404504056; SUP=cv%3D1%26bt%3D1404504056%26et%3D1404590456%26d%3Dc909%26i%3D6317%26us%3D1%26vf%3D0%26vt%3D0%26ac%3D%26st%3D0%26uid%3D2389429510%26name%3Djtiger%2540qq.com%26nick%3DJTiger0431%26fmp%3D%26lcp%3D; _s_tentry=passport.weibo.com; ".split(";");
        for (String c : cookieList) {
            String[] css = c.split("=");
            Cookie cookie = null;
            if (css.length == 1) {
                cookie = new Cookie(".weibo.com", css[0], "");
            } else {
                cookie = new Cookie(".weibo.com", css[0], css[1]);
            }
            //System.out.println("===" + cookie.toString());
            webClient.getCookieManager().addCookie(cookie);
        }
        final HtmlPage htmlPage = webClient.getPage("http://weibo.com/p/1002061729332983/weibo?page=1");
        webClient.waitForBackgroundJavaScript(100000);
        //System.out.println("=1="+htmlPage.getTitleText());
        //System.out.println("=2="+htmlPage.getWebResponse().getContentAsString());
        //System.out.println("=3="+htmlPage.asText());
    }
}
