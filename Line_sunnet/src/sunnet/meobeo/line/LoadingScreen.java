package sunnet.meobeo.line;

import sunnet.meobeo.framework.Game;
import sunnet.meobeo.framework.Graphics;
import sunnet.meobeo.framework.Graphics.PixmapFormat;
import sunnet.meobeo.framework.Screen;

public class LoadingScreen extends Screen{

	public LoadingScreen(Game game) {
		super(game);
	}
	
	@Override
	public void update(long deltaTime) {
		Graphics g = game.getGraphics();
/* man choi menu */
		Assets.bg = g.newPixmap("line_man.jpg", PixmapFormat.ARGB8888);
		Assets.bt_achievement = g.newPixmap("bt_achievement.png", PixmapFormat.ARGB8888);
		Assets.bt_achievement_ck = g.newPixmap("bt_achievement_ck.png", PixmapFormat.ARGB8888);
		Assets.bt_option = g.newPixmap("bt_option.png", PixmapFormat.ARGB8888);
		Assets.bt_option_ck = g.newPixmap("bt_option_ck.png", PixmapFormat.ARGB8888);
		Assets.bt_play = g.newPixmap("bt_play.png", PixmapFormat.ARGB8888);
		Assets.bt_play_ck = g.newPixmap("bt_play_ck.png", PixmapFormat.ARGB8888);
		
/* choi game */
		Assets.number = g.newPixmap("number.png", PixmapFormat.ARGB8888);
		Assets.background = g.newPixmap("background.jpg", PixmapFormat.ARGB8888);
		Assets.bg_game = g.newPixmap("bg_game.png", PixmapFormat.ARGB8888);
		Assets.bong = g.newPixmap("bong_to.png", PixmapFormat.ARGB8888);
		Assets.bong_nho = g.newPixmap("bong_nho.png", PixmapFormat.ARGB8888);
		Assets.undo = g.newPixmap("undo.png", PixmapFormat.ARGB8888);
		Assets.undo_click = g.newPixmap("undo_click.png", PixmapFormat.ARGB8888);
		Assets.bong_no = g.newPixmap("bong_no.png", PixmapFormat.ARGB8888);

/* hight score */
		Assets.ok_click = g.newPixmap("ok_click.png", PixmapFormat.ARGB8888);
		
/* game over */
		Assets.bg_menu = g.newPixmap("bg_menu.png", PixmapFormat.ARGB8888);
		Assets.menu = g.newPixmap("menu.png", PixmapFormat.ARGB8888);
		Assets.menu_click = g.newPixmap("menu_click.png", PixmapFormat.ARGB8888);
		Assets.left = g.newPixmap("left.png", PixmapFormat.ARGB8888);
		Assets.left_click = g.newPixmap("left_click.png", PixmapFormat.ARGB8888);
		Assets.right = g.newPixmap("right.png", PixmapFormat.ARGB8888);
		Assets.right_click = g.newPixmap("right_click.png", PixmapFormat.ARGB8888);
		
/* danh hieu */
		Assets.ga_mo = g.newPixmap("danh_hieu/ga_mo.jpg", PixmapFormat.ARGB8888);
		Assets.ga_dich = g.newPixmap("danh_hieu/ga_dich.jpg", PixmapFormat.ARGB8888);
		Assets.ga_que = g.newPixmap("danh_hieu/ga_que.jpg", PixmapFormat.ARGB8888);
		Assets.ga_thien = g.newPixmap("danh_hieu/ga_thien.jpg", PixmapFormat.ARGB8888);
		Assets.ga_toi = g.newPixmap("danh_hieu/ga_toi.jpg", PixmapFormat.ARGB8888);
		
		Assets.xuat_sac = g.newPixmap("danh_hieu/xuat_sac.jpg", PixmapFormat.ARGB8888);
		Assets.tuyet_voi = g.newPixmap("danh_hieu/tuyet_voi.jpg", PixmapFormat.ARGB8888);
		Assets.ngac_nhien = g.newPixmap("danh_hieu/ngac_nhien.jpg", PixmapFormat.ARGB8888);
		Assets.tot_dinh = g.newPixmap("danh_hieu/tot_dinh.jpg", PixmapFormat.ARGB8888);
		Assets.kiet_xuat = g.newPixmap("danh_hieu/kiet_xuat.jpg", PixmapFormat.ARGB8888);
		Assets.phi_thuong = g.newPixmap("danh_hieu/phi_thuong.jpg", PixmapFormat.ARGB8888);
		Assets.gioi_vai = g.newPixmap("danh_hieu/gioi_vai.jpg", PixmapFormat.ARGB8888);
		
/* am thanh */
		Assets.move = game.getAudio().newSound("move.mp3");
		Assets.boom1 = game.getAudio().newSound("boom1.mp3");
		Assets.boom2 = game.getAudio().newSound("boom2.mp3");
		Assets.boom3 = game.getAudio().newSound("boom3.wav");
		Assets.boom4 = game.getAudio().newSound("boom4.wav");
		Assets.touch = game.getAudio().newSound("touch.mp3");
		Assets.gameOver = game.getAudio().newSound("gameOver.mp3");
		Assets.clap = game.getAudio().newSound("clap.mp3");
		
		Settings.load(game.getFileIO());		
		game.setScreen(new MainMenuScreen(game));
	}

	@Override
	public void present(long deltaTime) {
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
	
}
