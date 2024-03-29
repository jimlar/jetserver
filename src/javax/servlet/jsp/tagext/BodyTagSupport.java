/*
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999 The Apache Software Foundation.  All rights 
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer. 
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:  
 *       "This product includes software developed by the 
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Tomcat", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written 
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */
package javax.servlet.jsp.tagext;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

import javax.servlet.*;

import java.io.Writer;

import java.util.Hashtable;

/**
 * Actions in a Tag Library are defined through subclasses of Tag.
 */

public class BodyTagSupport extends TagSupport implements BodyTag {

    /**
     * Default constructor, all subclasses are required to only define
     * a public constructor with the same signature, and to call the
     * superclass constructor.
     *
     * This constructor is called by the code generated by the JSP
     * translator.
     *
     */

    public BodyTagSupport() {
	super();
    }

    /**
     * Process the start tag for this instance.
     *
     * The doStartTag() method assumes that all setter methods have been
     * invoked before.
     *
     * When this method is invoked, the body has not yet been invoked.
     *
     * @returns EVAL_BODY_TAG if the tag wants to process body, SKIP_BODY if it
     * does ont want to process it.
     */
 
    public int doStartTag() throws JspException {
        return EVAL_BODY_TAG;
    }


    /**
     * Process the end tag. This method will be called on all Tag objects.
     *
     * All instance state associated with this instance must be reset.
     * The release() method should be called after this invocation.
     */

    public int doEndTag() throws JspException {
	return super.doEndTag();
    }


    // Actions related to body evaluation

    /**
     * Prepare for evaluation of the body
     * It will be invoked at most once per action invocation.
     * Will not be invoked if there is no body evaluation.
     *
     * Frequently it is not redefined by Tag author.
     *
     * @param b the BodyContent
     * @seealso #doAfterBody
     */

    public void setBodyContent(BodyContent b) {
	this.bodyContent = b;
    }

    /**
     * Prepare for evaluation of the body
     * It will be invoked at most once per action invocation.
     * Will not be invoked if there is no body evaluation.
     *
     * Frequently it is not redefined by Tag author.
     *
     * @seealso #doAfterBody
     */

    public void doInitBody() throws JspException {
    }

    /**
     * Actions after some body has been evaluated.
     *
     * Not invoked in empty tags or in tags returning SKIP_BODY in doStartTag()
     * This method is invoked after every body evaluation.
     * The pair "BODY -- doAfterBody()" is invoked initially if doStartTag()
     * returned EVAL_BODY_TAG, and it is repeated as long
     * as the doAfterBody() evaluation returns EVAL_BODY_TAG
     * <p>
     * The method re-invocations may be lead to different actions because
     * there might have been some changes to shared state, or because
     * of external computation.
     *
     * @returns whether additional evaluations of the body are desired
     * @seealso #doInitBody
     */

    public int doAfterBody() throws JspException {
 	return SKIP_BODY;
    }

    /**
     * reset the state of the Tag
     */

    public void release() {
	bodyContent = null;

	super.release();
    }

    /**
     * Get current bodyContent
     */
    
    public BodyContent getBodyContent() {
	return bodyContent;
    }


    /**
     * Get surrounding out
     */

    public JspWriter getPreviousOut() {
	return bodyContent.getEnclosingWriter();
    }

    // protected fields

    protected BodyContent   bodyContent;
}
