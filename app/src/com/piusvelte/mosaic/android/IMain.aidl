/*
 * Mosaic - Location Based Messaging
 * Copyright (C) 2013 Bryan Emmanuel
 * 
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *  
 *  Bryan Emmanuel piusvelte@gmail.com
 */
package com.piusvelte.mosaic.android;

interface IMain {
	void setCoordinates(double latitude, double longitude);
	void setNickname(String nickname);
	void addMessage(long id, double latitude, double longitude, String title, String body, String nick);
	void clearMessages();
	void viewMessage(long id, String title, String body, String nick);
	void editMessage(long id, String title, String body, int radius, long expiry);
	void updateMarker(long id, String title, String body, String nick);
}