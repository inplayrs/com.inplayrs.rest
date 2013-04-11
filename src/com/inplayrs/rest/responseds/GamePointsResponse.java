package com.inplayrs.rest.responseds;

public class GamePointsResponse {


	// From game table
	private int global_pot_size;
	private int fangroup_pot_size;
	
	
	// from global_game_leaderboard table
	private int points;
	private int global_rank;
	
	// from fangroup_game_leaderboard table
	private int fangroup_name;
	private int fangroup_rank;
	
	// from h2h_pool
	private String h2h_user;
	private int h2h_pot_size;
	
	// from user_in_fangroup_game_leaderboard
	private int userinfangroupRank;
	
	// from game_entry
	private int total_winnings;
	
	
	public GamePointsResponse() {
		/*
		 * 

select 
	ge.user,
	ge.game as game_id,
	g.global_pot_size,
	g.fangroup_pot_size,
	h2h.h2h_pot_size,
	ge.total_winnings,
	(CASE 
    WHEN h2h.game_entry_1 = ge.game_entry_id then h2h.user_2
    WHEN h2h.game_entry_2 = ge.game_entry_id then h2h.user_1
    ELSE null
	END) as h2h_user,
	ggl.fangroup_name as fangroup_name,
	ggl.points,
	ggl.rank as global_rank,
	fgl.rank as fangroup_rank,
	uifgl.rank as user_in_fangroup_rank
from 
	game_entry ge
	left join game g on g.game_id = ge.game
	left join h2h_pool h2h on ( (h2h.game_entry_1 = ge.game_entry_id) or
										 (h2h.game_entry_2 = ge.game_entry_id) )
	left join global_game_leaderboard ggl on (ggl.game = ge.game and ggl.user = ge.user)
	left join fangroup_game_leaderboard fgl on (fgl.game = ge.game and fgl.fangroup_id = ggl.fangroup_id)
	left join user_in_fangroup_game_leaderboard uifgl on (uifgl.game = ge.game and uifgl.user = ge.user)
	
										 
										 
		 */
	}
	
	
	
	
	
}
