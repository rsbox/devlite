/*
 * Copyright (c) 2017, Adam <Adam@sigterm.info>
 * Copyright (c) 2020, ThatGamerBlue <thatgamerblue@gmail.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.plugins.xtea;

import java.io.IOException;
import java.util.HashMap;
import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.http.api.xtea.XteaClient;
import org.apache.commons.lang3.ArrayUtils;
import okhttp3.OkHttpClient;

@PluginDescriptor(
	name = "Xtea",
	hidden = true
)
@Slf4j
public class XteaPlugin extends Plugin
{
	@Inject
	private XteaClient xteaClient;

	@Inject
	private Client client;

	private HashMap<Integer, Integer[]> xteas;

	@Override
	public void startUp()
	{
		try
		{
			xteas = xteaClient.get();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Provides
	XteaClient provideXteaClient(OkHttpClient okHttpClient)
	{
		return new XteaClient(okHttpClient);
	}

	@Subscribe
	private void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() != GameState.LOGGED_IN)
		{
			return;
		}

		int[] regions = client.getMapRegions();
		int[][] xteaKeys = client.getXteaKeys();

		for (int idx = 0; idx < regions.length; ++idx)
		{
			int region = regions[idx];
			int[] keys = xteaKeys[idx];

			if (xteas.get(region) != null && areKeysEqual(xteas.get(region), keys))
			{
				continue;
			}

			xteas.put(region, ArrayUtils.toObject(keys));

			log.info("Submitting region {} keys {}, {}, {}, {}", region, keys[0], keys[1], keys[2], keys[3]);

			//Don't post non encrypted regions
			if (keys[0] == 0 && keys[1] == 0 && keys[2] == 0 && keys[3] == 0)
			{
				continue;
			}

			xteaClient.submit(region, keys);
		}
	}

	private boolean areKeysEqual(Integer[] existingKeys, int[] newKeys)
	{
		return existingKeys[0] == newKeys[0] &&
			existingKeys[1] == newKeys[1] &&
			existingKeys[2] == newKeys[2] &&
			existingKeys[3] == newKeys[3];
	}
}