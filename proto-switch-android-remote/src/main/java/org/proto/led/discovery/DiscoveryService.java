/*
 * *****************************************************************
 *   This file is part of PROTO-SWITCH Light controller project.
 *
 *   Copyright (C) 2016 ViP-PROTO Association, http://vip-proto.com
 *   Predrag Milutinovic <pedjolino@gmail.com>
 *
 *   This program is free software; you can redistribute it and/or
 *   modify it under the terms of the GNU General Public License as
 *   published by the Free Software Foundation; either version 2 of the
 *   License, or (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful, but
 *   WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *   General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program; if not, write to the Free Software
 *   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 *   02111-1307, USA.
 *
 *   The GNU General Public License is contained in the file COPYING.
 * /
 *
 */

package org.proto.led.discovery;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class DiscoveryService extends IntentService {
	// TODO: Rename actions, choose action names that describe tasks that this
	// IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
	private static final String ACTION_FOO = "org.proto.led.discovery.action.FOO";
	private static final String ACTION_BAZ = "org.proto.led.discovery.action.BAZ";

	// TODO: Rename parameters
	private static final String EXTRA_PARAM1 = "org.proto.led.discovery.extra.PARAM1";
	private static final String EXTRA_PARAM2 = "org.proto.led.discovery.extra.PARAM2";

	/**
	 * Starts this service to perform action Foo with the given parameters. If
	 * the service is already performing a task this action will be queued.
	 *
	 * @see IntentService
	 */
	// TODO: Customize helper method
	public static void startActionFoo(Context context, String param1, String param2) {
		Intent intent = new Intent( context, DiscoveryService.class );
		intent.setAction( ACTION_FOO );
		intent.putExtra( EXTRA_PARAM1, param1 );
		intent.putExtra( EXTRA_PARAM2, param2 );
		context.startService( intent );
	}

	/**
	 * Starts this service to perform action Baz with the given parameters. If
	 * the service is already performing a task this action will be queued.
	 *
	 * @see IntentService
	 */
	// TODO: Customize helper method
	public static void startActionBaz(Context context, String param1, String param2) {
		Intent intent = new Intent( context, DiscoveryService.class );
		intent.setAction( ACTION_BAZ );
		intent.putExtra( EXTRA_PARAM1, param1 );
		intent.putExtra( EXTRA_PARAM2, param2 );
		context.startService( intent );
	}

	public DiscoveryService() {
		super( "DiscoveryService" );
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if (intent != null) {
			final String action = intent.getAction();
			if (ACTION_FOO.equals( action )) {
				final String param1 = intent.getStringExtra( EXTRA_PARAM1 );
				final String param2 = intent.getStringExtra( EXTRA_PARAM2 );
				handleActionFoo( param1, param2 );
			} else if (ACTION_BAZ.equals( action )) {
				final String param1 = intent.getStringExtra( EXTRA_PARAM1 );
				final String param2 = intent.getStringExtra( EXTRA_PARAM2 );
				handleActionBaz( param1, param2 );
			}
		}
	}

	/**
	 * Handle action Foo in the provided background thread with the provided
	 * parameters.
	 */
	private void handleActionFoo(String param1, String param2) {
		// TODO: Handle action Foo
		throw new UnsupportedOperationException( "Not yet implemented" );
	}

	/**
	 * Handle action Baz in the provided background thread with the provided
	 * parameters.
	 */
	private void handleActionBaz(String param1, String param2) {
		// TODO: Handle action Baz
		throw new UnsupportedOperationException( "Not yet implemented" );
	}
}
