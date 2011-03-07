package org.esgf.accounts;

import org.esgf.accounts.StringValidationUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for {@link StringValidationUtils}.
 * 
 * @author Luca Cinquini
 *
 */
public class StringValidationUtilsTest {
    
    @Test
    public void testIsNotAlphanumeric() {
        
        Assert.assertFalse(StringValidationUtils.isNotAlphanumeric("aBc123"));
        Assert.assertTrue(StringValidationUtils.isNotAlphanumeric("%abc123"));
        Assert.assertTrue(StringValidationUtils.isNotAlphanumeric("$abc123"));
        Assert.assertTrue(StringValidationUtils.isNotAlphanumeric("ab*c123"));
        Assert.assertTrue(StringValidationUtils.isNotAlphanumeric("ab c123"));
        Assert.assertTrue(StringValidationUtils.isNotAlphanumeric("ab.c123"));
        
    }
    
    @Test
    public void testHasOneUpperCaseLetter() {
        Assert.assertTrue(StringValidationUtils.hasOneUpperCaseLetter("aBc123"));
        Assert.assertFalse(StringValidationUtils.hasOneUpperCaseLetter("abc123"));
    }
    
    @Test
    public void testHasOneLowerCaseLetter() {
        Assert.assertTrue(StringValidationUtils.hasOneLowerCaseLetter("aBc123"));
        Assert.assertFalse(StringValidationUtils.hasOneLowerCaseLetter("ABC123"));
    }
    
    @Test
    public void testHasOneNumber() {
        Assert.assertTrue(StringValidationUtils.hasOneNumber("aBc123"));
        Assert.assertFalse(StringValidationUtils.hasOneNumber("ABC"));
    }
    
    @Test
    public void testHasInvalidCharacters() {
        Assert.assertTrue(StringValidationUtils.hasInvalidCharacters("abc>123"));
        Assert.assertTrue(StringValidationUtils.hasInvalidCharacters("abc<123"));
        Assert.assertTrue(StringValidationUtils.hasInvalidCharacters("ab#>123"));
        Assert.assertTrue(StringValidationUtils.hasInvalidCharacters("abc$123"));
        Assert.assertTrue(StringValidationUtils.hasInvalidCharacters("abc&123"));
        Assert.assertTrue(StringValidationUtils.hasInvalidCharacters("abc!123"));
        Assert.assertTrue(StringValidationUtils.hasInvalidCharacters("abc\\/123"));
        Assert.assertTrue(StringValidationUtils.hasInvalidCharacters("abc\\123"));
        Assert.assertTrue(StringValidationUtils.hasInvalidCharacters("abc=123"));
        Assert.assertTrue(StringValidationUtils.hasInvalidCharacters("abc+123"));
        Assert.assertFalse(StringValidationUtils.hasInvalidCharacters("aBc12*^-.?"));
    }
    
    @Test
    public void testIsEmail() {
        Assert.assertTrue(StringValidationUtils.isEmail("a@b.c"));
        Assert.assertFalse(StringValidationUtils.isEmail("ab.c"));
        Assert.assertFalse(StringValidationUtils.isEmail("@ab.c"));
        Assert.assertFalse(StringValidationUtils.isEmail("ab.c@"));
        Assert.assertFalse(StringValidationUtils.isEmail("a@b"));
        Assert.assertFalse(StringValidationUtils.isEmail("@"));
    }

}
