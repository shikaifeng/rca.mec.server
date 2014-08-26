<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<input type="hidden" name="pageNumber" id="pageNumber" value="1" />
<input type="hidden" name="pageSize" id="pageSize" value="10" />
<input type="hidden" name="totalPage" id="totalPage" value="0" />
	   	  	
<a href="javascript:setPageNumber('first')" class="last unoper">首页</a>
<a href="javascript:setPageNumber('previou')" class="next">上一页</a>
<span id="pageShowId">0/0页</span>
<a href="javascript:setPageNumber('next')" class="next">下一页</a>
<a href="javascript:setPageNumber('last')" class="last">尾页</a>
<span>第 <input name="tPageNumber" id="tPageNumber" type="text" /> 页 <a type="submit" onclick="setPageNumber($('#tPageNumber').val())">GO</a></span>