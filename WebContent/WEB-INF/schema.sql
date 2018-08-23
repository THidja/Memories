create table if not exists `user` ( 
	`user_id` INTEGER primary key auto_increment,	
	`username` varchar(50) not null unique,
	`password` varchar(256) not null,
	`nom` 	varchar(255) not null,
	`prenom` varchar(255) not null );
	

create table if not exists `freind` (
	`user_id1` INTEGER,
	`user_id2` INTEGER,
	 primary key (user_id1,user_id2),
	 foreign key (user_id1) references user(user_id) on update cascade on delete cascade,
	 foreign key (user_id2) references user(user_id) on update cascade on delete cascade );

)
create table if not exists `session` (
	`key` varchar(256),
	`user_id` INTEGER unique not null,
	`expiration` timestamp not null,
	`root` bool default false,
	 primary key(`key`),
	 foreign key (user_id) references user(user_id) on update cascade on delete cascade);