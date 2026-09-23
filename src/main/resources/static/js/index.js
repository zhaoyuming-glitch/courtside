$(document).ready(function () {

    // ===== 头像点击弹出菜单 =====
    $('#navAvatar').on('click', function (e) {
        e.stopPropagation();
        $('#navUserMenu').toggleClass('show');
    });

    // 点空白处收起菜单
    $(document).on('click', function () {
        $('#navUserMenu').removeClass('show');
    });

    // 页面加载后，尝试获取当前登录用户，更新头像首字母
    $.get('/api/currentUser', function (resp) {
        if (resp && resp.username) {
            $('#navAvatar').text(resp.username.charAt(0).toUpperCase());
        } else {
            $('#navAvatar').text('?');
        }
    }).fail(function () {
        // 没这个接口就算了，保持默认 K
    });

    // ===== 首页比赛数据 =====
    $.get("/api/games/scoreboard", function (resp) {
        $('.loading-spinner').hide();
        $('.hero').removeClass('has-games no-games');

        if (!resp || resp.length === 0) {
            $('.hero').addClass('no-games');
            $('.hero').addClass('loaded');
            return;
        }

        $('.hero').addClass('has-games');

        var g = resp[0];
        $('.score-strip').data("game_id", g.id);

        var tagMap = { "in_progress": "LIVE MATCH", "scheduled": "UPCOMING", "final": "FINAL" };
        var clsMap = { "in_progress": "live", "scheduled": "upcoming", "final": "final" };
        var state = g.status_state || "final";
        $(".hero-tag").text(tagMap[state] || "UNKNOWN")
            .removeClass("live final upcoming")
            .addClass(clsMap[state] || "");

        var info = g.home_team.name + " VS " + g.visitor_team.name;
        if (state === "in_progress") {
            info += " · 第" + g.period + "节 " + g.time;
        } else if (state === "scheduled") {
            info += " · 未开始";
        } else {
            info += " · 已结束";
        }
        $(".hero-info").text(info);

        var homeWin = g.home_team_score > g.visitor_team_score;

        $(".score-strip .score-block:eq(0) .team-name").text(g.home_team.name);
        $(".score-strip .score-block:eq(0) .team-record").text("-");
        var hScore = $(".score-strip .score-block:eq(0) .score-num").text(g.home_team_score).removeClass("win");
        if (homeWin) hScore.addClass("win");

        $(".score-strip .score-block:eq(1) .team-name").text(g.visitor_team.name);
        $(".score-strip .score-block:eq(1) .team-record").text("-");
        var vScore = $(".score-strip .score-block:eq(1) .score-num").text(g.visitor_team_score).removeClass("win");
        if (!homeWin && g.home_team_score !== g.visitor_team_score) vScore.addClass("win");

        var smalls = resp.slice(1, 4);
        $(".sec-card").hide();

        for (var i = 0; i < smalls.length && i < 3; i++) {
            var s = smalls[i];
            var card = $(".sec-card:eq(" + i + ")").show();
            card.data("game_id", s.id);

            var sHomeWin = s.home_team_score > s.visitor_team_score;
            var sVisitorWin = s.visitor_team_score > s.home_team_score;

            card.find(".sec-team-left .team-name").text(s.home_team.name);
            var lScore = card.find(".sec-team-left .sec-score").text(s.home_team_score).removeClass("leading");
            if (sHomeWin) lScore.addClass("leading");

            card.find(".sec-team-right .team-name").text(s.visitor_team.name);
            var rScore = card.find(".sec-team-right .sec-score").text(s.visitor_team_score).removeClass("leading");
            if (sVisitorWin) rScore.addClass("leading");

            var sState = s.status_state || "final";
            var smallTagMap = { "in_progress": "LIVE Q" + (s.period || ""), "scheduled": "UPCOMING", "final": "FINAL" };
            var smallClsMap = { "in_progress": "live", "scheduled": "upcoming", "final": "final" };
            card.find(".sec-status").text(smallTagMap[sState] || "UNKNOWN")
                .removeClass("live final upcoming")
                .addClass(smallClsMap[sState] || "");
        }

        $('.score-strip').on("click", function () {
            var data = $(this).data('game_id');
            if (data) {
                window.location.href = "/gamedetails?game_id=" + data;
            }
        });

        $('.secondary-scores').on('click', '.sec-card', function () {
            var data1 = $(this).data("game_id");
            if (data1) {
                window.location.href = "/gamedetails?game_id=" + data1;
            }
        });

        $('.hero').addClass('loaded');

    }).fail(function () {
        $('.loading-spinner').hide();
        $('.hero').removeClass('has-games no-games');
        $('.hero').addClass('no-games');
        $(".no-game-placeholder").html(`
            <div style="font-size:80px;">⚠️</div>
            <h2 style="font-size:32px; font-weight:900; text-transform:uppercase; letter-spacing:-1px;">加载失败</h2>
            <p style="font-size:16px; font-weight:700; color:#999; margin-top:12px;">请检查后端服务是否启动</p>
            <div style="margin-top:30px;">
                <button onclick="location.reload()" style="padding:12px 32px; background:#0a0a0a; color:#fff; font-weight:900; font-size:14px; text-transform:uppercase; border:3px solid #0a0a0a; cursor:pointer; transition:0.2s;">🔄 重新加载</button>
            </div>
        `);
        $('.hero').addClass('loaded');
    });
});