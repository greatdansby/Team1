/**
 * 
 */
package us.monoid.web.jp.javacc;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.List;

import org.junit.Test;

import us.monoid.json.JSONArray;
import us.monoid.json.JSONException;
import us.monoid.json.JSONObject;
import us.monoid.json.JSONTokener;
import us.monoid.web.jp.javacc.JSONPathCompiler;
import us.monoid.web.jp.javacc.ParseException;
import us.monoid.web.jp.javacc.JSONPathCompiler.JSONPathExpr;


/**
 * @author beders
 *
 */
public class JSONPathCompilerTest {
	
	
	@Test
	public void testRepeat() throws Exception {
		for (int i = 0; i < 10; i++) {
			System.out.println("RUN      " + i);
			testGeo();
		}
	}
	
	protected void testGeo() throws Exception {
		System.out.println(this.getClass().getResource("geo.json"));
		JSONObject json = read(this.getClass().getResource("geo.json"));
		System.out.println(json);
    JSONPathExpr evaluator = parse("results[address_components.short_name='U.S. 101'].formatted_address");
    Object result = evaluator.eval(json);
    System.out.println(result);
    assertTrue(result.toString().contains("Golden Gate Bridge"));
	}

	@Test
	public void testSimplePath() throws Exception {
		System.out.println(this.getClass().getResource("books.json"));
		JSONObject json = read(this.getClass().getResource("books.json"));

    JSONPathExpr evaluator = parse("store.book.reviewed-by");
    Object result = evaluator.eval(json);
    System.out.println(result);
    assertTrue(result instanceof List);
	}

	/**
	 * Test method for {@link us.monoid.web.jp.javacc.JSONPathCompiler#JSONPathCompiler(java.io.InputStream, java.lang.String)}.
	 * @throws ParseException 
	 * @throws JSONException 
	 */
	@Test
	public void testJSONPathCompiler() throws ParseException, JSONException {
		System.out.println(this.getClass().getResource("books.json"));
		JSONObject json = read(this.getClass().getResource("books.json"));
		JSONPathExpr evaluator = parse("store.book[0].author");
		Object result = evaluator.eval(json);
		System.out.println(result);
		assertEquals("Nigel Rees", result);
		
		evaluator = parse("store.book");
		result = evaluator.eval(json);
		System.out.println(result);
		assertTrue(result instanceof JSONArray);
		evaluator = parse("store.book[category='fiction'].author");
		result = evaluator.eval(json);
    System.out.println(result);
    assertEquals("Evelyn Waugh", result);
    
    evaluator = parse("store.book[!category='reference'].author");
		result = evaluator.eval(json);
    System.out.println(result);
    assertEquals("Evelyn Waugh", result);
    
    evaluator = parse("store.book[price>9].author");
		result = evaluator.eval(json);
    System.out.println(result);
    assertEquals("Evelyn Waugh", result);
    
    evaluator = parse("store.book[price>12.999].author");
		result = evaluator.eval(json);
    System.out.println(result);
    assertEquals("J. R. R. Tolkien", result);
    
    evaluator = parse("store.book[price>9 && price<12.999].author");
		result = evaluator.eval(json);
    System.out.println(result);
    assertEquals("Evelyn Waugh", result);
    
    evaluator = parse("store.book[reviewed-by.name='JB'].author");
		result = evaluator.eval(json);
    System.out.println(result);
    assertEquals("Nigel Rees", result); 
    
    
    
//    evaluator = parse("store.book[vendors[name='Amazon']].author");
//		result = evaluator.eval(json);
//    System.out.println(result);
//    assertEquals("Evelyn Waugh", result); 
	}
	
	JSONPathCompiler.JSONPathExpr parse(String path) throws ParseException {
		java.io.StringReader r = new java.io.StringReader(path);
    JSONPathCompiler app = new JSONPathCompiler(r);
    JSONPathCompiler.JSONPathExpr x = JSONPathCompiler.JSONPathExpr.class.cast(app.json());
    x.dump(" ");
    return x;
	}

	JSONObject read(URL testFile) {
		try {
			return new JSONObject(new JSONTokener(new InputStreamReader(testFile.openStream(), "UTF-8")));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		return null;
	}
	
	@Test
	public void testArray() throws ParseException, JSONException {
		System.out.println(this.getClass().getResource("array-test.json"));
		JSONObject json = read(this.getClass().getResource("array-test.json"));
		JSONPathExpr evaluator = parse("backdrops[image.width=1280]");
		Object result = evaluator.eval(json);
		System.out.println(result);
	}
	
}
