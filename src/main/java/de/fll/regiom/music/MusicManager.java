package de.fll.regiom.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public enum MusicManager {
	INSTANCE;

	private final AudioPlayerManager playerManager;
	private final AudioPlayer player;
	private final TrackScheduler scheduler;

	MusicManager() {
		playerManager = new DefaultAudioPlayerManager();
		AudioSourceManagers.registerRemoteSources(playerManager);
		player = playerManager.createPlayer();
		scheduler = new TrackScheduler(player);
		player.addListener(scheduler);
	}

	public void loadAndPlay(String trackUrl) {
		playerManager.loadItemOrdered(this, trackUrl, new AudioLoadResultHandler() {
			@Override
			public void trackLoaded(AudioTrack audioTrack) {
				System.out.println("Track loaded: " + audioTrack);
				play(audioTrack);
			}

			@Override
			public void playlistLoaded(AudioPlaylist audioPlaylist) {
				AudioTrack firstTrack = audioPlaylist.getSelectedTrack();

				if (firstTrack == null) {
					firstTrack = audioPlaylist.getTracks().get(0);
				}

				play(firstTrack);

			}

			@Override
			public void noMatches() {
				System.out.println("No matches!");
			}

			@Override
			public void loadFailed(FriendlyException e) {
				System.out.println("Load failed: " + e);
			}
		});
	}

	public void skip() {
		scheduler.nextTrack();
	}

	private void play(AudioTrack audioTrack) {
		scheduler.queue(audioTrack);
		player.setVolume(15);
	}

	public AudioPlayerSendHandler getSendHandler() {
		return new AudioPlayerSendHandler(player);
	}

}
