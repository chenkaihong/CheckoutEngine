/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
var app = {
	// Application Constructor
	initialize: function() {
		this.bindEvents();
	},
	// Bind Event Listeners
	//
	// Bind any events that are required on startup. Common events are:
	// 'load', 'deviceready', 'offline', and 'online'.
	bindEvents: function() {
		document.addEventListener('deviceready', app.onDeviceReady, false);
	},
	// deviceready Event Handler
	//
	// The scope of 'this' is the event. In order to call the 'receivedEvent'
	// function, we must explicitly call 'app.receivedEvent(...);'
	onDeviceReady: function() {		
		var html = '<div data-role="collapsible" data-inset="false">';
		html += '<h4>cordova</h4>';
		html += '<ul data-role="listview">';
		html += '<li>' + device.cordova + '</li>';		
		html += '</ul></div>';
 
		html += '<div data-role="collapsible" data-inset="false">';
		html += '<h4>platform</h4>';
		html += '<ul data-role="listview">';
		html += '<li>' + device.platform + ' ' + device.version + '</li>';
		html += '<li>' + device.model + '</li>';
		html += '</ul></div>';
 
		$('#dev')[0].innerHTML = html;		
		$('[data-role=collapsible]').collapsible().trigger('create');		
		// 这句激活所有 collapsible 标签的按钮，如果没有的话，添加的内容只会以原始的 ul/li 的方式呈现。
	}	
};