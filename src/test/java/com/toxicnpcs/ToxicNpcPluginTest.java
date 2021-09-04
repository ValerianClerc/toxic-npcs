package com.toxicnpcs;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class ToxicNpcPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(ToxicNpcPlugin.class);
		RuneLite.main(args);
	}
}