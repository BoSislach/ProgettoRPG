import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;

public class GameGUI extends JFrame {

    
    static final Color BG_DARK = new Color(10, 10, 18);
    static final Color BG_PANEL = new Color(18, 18, 30);
    static final Color BG_CARD = new Color(28, 28, 44);
    static final Color BG_CARD_HOVER = new Color(38, 38, 56);
    static final Color BG_INPUT = new Color(14, 14, 24);
    static final Color ACCENT = new Color(232, 69, 69);
    static final Color ACCENT_SOFT = new Color(255, 107, 107);
    static final Color ACCENT_BLUE = new Color(91, 141, 238);
    static final Color ACCENT_GREEN = new Color(34, 197, 94);
    static final Color ACCENT_YELLOW = new Color(245, 166, 35);
    static final Color ACCENT_PURPLE = new Color(168, 85, 247);
    static final Color ACCENT_CYAN = new Color(78, 205, 196);
    static final Color TEXT = new Color(232, 232, 239);
    static final Color TEXT_DIM = new Color(140, 140, 165);
    static final Color TEXT_MUTED = new Color(85, 85, 110);
    static final Color BORDER_COLOR = new Color(255, 255, 255, 18);
    static final Color HP_RED = new Color(239, 68, 68);

    
    static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 20);
    static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.BOLD, 15);
    static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 13);
    static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);
    static final Font FONT_MONO = new Font("Consolas", Font.PLAIN, 12);
    static final Font FONT_MONO_SM = new Font("Consolas", Font.PLAIN, 11);
    static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 11);
    static final Font FONT_LOGO = new Font("Segoe UI", Font.BOLD, 14);
    static final Font FONT_BIG = new Font("Segoe UI", Font.BOLD, 28);

    
    enum GameState {
        INTRO, EXPLORING, COMBAT_PLAYER, COMBAT_WEAPON, COMBAT_POTION,
        COMBAT_ENEMY, GAME_OVER, ENDING
    }

    private World World;
    private GameCharacter protagonist;
    private GameState state = GameState.INTRO;

    private final Set<String> visitedLocations = new HashSet<>();
    private final Set<String> defeatedEnemies = new HashSet<>();
    private static final Set<String> MAIN_BOSSES = Set.of(
            "Luca Vassena", "The Junkie", "The Wall", "The Bangla");

    
    private Enemy combatEnemy;
    private Location combatLocation;
    private boolean playerTurn;
    private Location previousLocation;
    private String endingEpilogue = "";

    
    private CardLayout mainCardLayout;
    private JPanel mainCardPanel;

    
    private JPanel introPanel;

    
    private JPanel explorationPanel;
    private MinimapPanel minimapPanel;
    private JLabel locNameLabel;
    private JLabel locDescLabel;
    private JPanel eventBanner;
    private JLabel eventLabel;
    private JPanel enemiesListPanel;
    private JPanel enemiesSectionPanel;
    private JPanel itemsListPanel;
    private JPanel itemsSectionPanel;
    private JPanel exitsListPanel;
    private JTextArea gameLogArea;
    private JPanel endingButtonPanel;

    
    private JLabel statHP, statSTR, statATK, statDEF, statXP, statLVL;
    private JProgressBar hpBarStat;
    private JLabel shieldLabel;
    private JPanel shieldPanel;
    private JPanel inventoryListPanel;
    private JLabel weightLabel;

    
    private JLabel topLocLabel;
    private JProgressBar topHPBar;
    private JLabel topHPVal, topXPVal, topLVLVal;

    
    private JPanel combatPanel;
    private JLabel combatPlayerNameLabel;
    private JLabel combatEnemyNameLabel;
    private JProgressBar combatPlayerHPBar;
    private JProgressBar combatEnemyHPBar;
    private JLabel combatPlayerHPText;
    private JLabel combatEnemyHPText;
    private JLabel combatTurnLabel;
    private JTextArea combatLogArea;
    private JPanel combatActionsPanel;
    private JPanel weaponSelectPanel;
    private JPanel potionSelectPanel;
    private JPanel spellSelectPanel;
    private JPanel combatMainActions;

    
    private int burnTurns = 0; 
    private int freezeTurns = 0; 

    
    private int chargesFire = 2;    
    private int chargesLightning = 1; 
    private int chargesIce = 1;     

    
    private JPanel gameOverPanel;
    private JLabel gameOverTitle;
    private JLabel gameOverText;

    
    private static final Map<String, double[]> MINIMAP_POS = new LinkedHashMap<>();
    static {
        MINIMAP_POS.put("stazione", new double[] { 0.08, 0.08 });
        MINIMAP_POS.put("Roma Street", new double[] { 0.28, 0.08 });
        MINIMAP_POS.put("Delicious Kebab", new double[] { 0.50, 0.04 });
        MINIMAP_POS.put("Roma Street - Brawl", new double[] { 0.52, 0.18 });
        MINIMAP_POS.put("Tobacco Shop Back", new double[] { 0.78, 0.26 });
        MINIMAP_POS.put("Underground Tunnels", new double[] { 0.62, 0.38 });
        MINIMAP_POS.put("Underground Parking", new double[] { 0.38, 0.44 });
        MINIMAP_POS.put("Abandoned Building", new double[] { 0.18, 0.54 });
        MINIMAP_POS.put("Building Rooftop", new double[] { 0.12, 0.68 });
        MINIMAP_POS.put("Central Zone", new double[] { 0.38, 0.76 });
        MINIMAP_POS.put("End of the Street", new double[] { 0.62, 0.84 });
        MINIMAP_POS.put("Mix Market", new double[] { 0.85, 0.93 });
    }

    
    
    private static final Map<String, String> LOCATION_ICONS = new LinkedHashMap<>();
    static {
        LOCATION_ICONS.put("stazione", "[S]");
        LOCATION_ICONS.put("Roma Street", "[V]");
        LOCATION_ICONS.put("Delicious Kebab", "[K]");
        LOCATION_ICONS.put("Roma Street - Brawl", "[R]");
        LOCATION_ICONS.put("Tobacco Shop Back", "[T]");
        LOCATION_ICONS.put("Underground Tunnels", "[P]");
        LOCATION_ICONS.put("Underground Parking", "[P]");
        LOCATION_ICONS.put("Abandoned Building", "[E]");
        LOCATION_ICONS.put("Building Rooftop", "[^]");
        LOCATION_ICONS.put("Central Zone", "[Z]");
        LOCATION_ICONS.put("End of the Street", "[F]");
        LOCATION_ICONS.put("Mix Market", "[M]");
    }

    
    public GameGUI() {
        super("Roma Street: Street War");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1280, 800);
        setMinimumSize(new Dimension(1000, 650));
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_DARK);

        initUI();
        showIntro();
    }

    
    private void initUI() {
        setLayout(new BorderLayout());

        mainCardLayout = new CardLayout();
        mainCardPanel = new JPanel(mainCardLayout);
        mainCardPanel.setBackground(BG_DARK);

        
        introPanel = createIntroPanel();
        mainCardPanel.add(introPanel, "INTRO");

        
        JPanel gamePanel = new JPanel(new BorderLayout());
        gamePanel.setBackground(BG_DARK);

        JPanel topBar = createTopBar();
        gamePanel.add(topBar, BorderLayout.NORTH);

        JPanel columnsPanel = new JPanel(new BorderLayout());
        columnsPanel.setBackground(BG_DARK);

        
        JPanel leftPanel = createLeftPanel();
        columnsPanel.add(leftPanel, BorderLayout.WEST);

        
        JPanel centerPanel = createCenterPanel();
        columnsPanel.add(centerPanel, BorderLayout.CENTER);

        
        JPanel rightPanel = createRightPanel();
        columnsPanel.add(rightPanel, BorderLayout.EAST);

        gamePanel.add(columnsPanel, BorderLayout.CENTER);
        mainCardPanel.add(gamePanel, "GAME");

        
        gameOverPanel = createGameOverPanel();
        mainCardPanel.add(gameOverPanel, "GAMEOVER");

        add(mainCardPanel, BorderLayout.CENTER);
    }

    
    private JPanel createIntroPanel() {
        JPanel p = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();
                g2.setColor(BG_DARK);
                g2.fillRect(0, 0, w, h);
                
                RadialGradientPaint rg = new RadialGradientPaint(
                        new Point2D.Float(w * 0.3f, h * 0.5f),
                        w * 0.5f,
                        new float[] { 0f, 1f },
                        new Color[] { new Color(232, 69, 69, 18), new Color(0, 0, 0, 0) });
                g2.setPaint(rg);
                g2.fillRect(0, 0, w, h);
                RadialGradientPaint rg2 = new RadialGradientPaint(
                        new Point2D.Float(w * 0.7f, h * 0.3f),
                        w * 0.4f,
                        new float[] { 0f, 1f },
                        new Color[] { new Color(91, 141, 238, 12), new Color(0, 0, 0, 0) });
                g2.setPaint(rg2);
                g2.fillRect(0, 0, w, h);
            }
        };
        p.setLayout(new GridBagLayout());
        p.setBackground(BG_DARK);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);

        
        JLabel badge = styledLabel("TEXT RPG", FONT_HEADER, ACCENT);
        badge.setAlignmentX(Component.CENTER_ALIGNMENT);
        badge.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(232, 69, 69, 60), 1, true),
                new EmptyBorder(4, 18, 4, 18)));
        content.add(badge);
        content.add(Box.createVerticalStrut(20));

        
        JLabel title1 = styledLabel("Roma Street", new Font("Segoe UI", Font.BOLD, 52), TEXT);
        title1.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(title1);

        
        JPanel sep = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth();
                GradientPaint gp = new GradientPaint(0, 0, new Color(0, 0, 0, 0), w / 2, 0, ACCENT);
                g2.setPaint(gp);
                g2.fillRect(0, 0, w / 2, 2);
                gp = new GradientPaint(w / 2, 0, ACCENT, w, 0, new Color(0, 0, 0, 0));
                g2.setPaint(gp);
                g2.fillRect(w / 2, 0, w / 2, 2);
            }
        };
        sep.setOpaque(false);
        sep.setPreferredSize(new Dimension(160, 2));
        sep.setMaximumSize(new Dimension(160, 2));
        sep.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(Box.createVerticalStrut(8));
        content.add(sep);
        content.add(Box.createVerticalStrut(8));

        JLabel title2 = styledLabel("STREET WAR", new Font("Segoe UI", Font.BOLD, 18), TEXT_DIM);
        title2.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(title2);
        content.add(Box.createVerticalStrut(20));

        JLabel sub = styledLabel(
                "<html><center>Violent night on the street. Scooters, screams and distant sirens.<br>You ended up in the wrong place... or maybe the right one.</center></html>",
                FONT_BODY, TEXT_DIM);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);
        sub.setHorizontalAlignment(SwingConstants.CENTER);
        content.add(sub);
        content.add(Box.createVerticalStrut(30));

        
        JPanel statsRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 0));
        statsRow.setOpaque(false);
        statsRow.add(introStatBox("12", "Locations"));
        statsRow.add(introStatBox("4", "Boss"));
        statsRow.add(introStatBox("3", "Endings"));
        content.add(statsRow);
        content.add(Box.createVerticalStrut(35));

        
        JButton startBtn = createAccentButton("START ADVENTURE", 20);
        startBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        startBtn.addActionListener(e -> startGame());
        content.add(startBtn);
        content.add(Box.createVerticalStrut(16));

        JLabel hint = styledLabel("Press ENTER to start", FONT_SMALL, TEXT_MUTED);
        hint.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(hint);

        p.add(content);

        
        p.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "start");
        p.getActionMap().put("start", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });

        return p;
    }

    private JPanel introStatBox(String num, String label) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        JLabel n = styledLabel(num, FONT_BIG, TEXT);
        n.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel l = styledLabel(label, FONT_SMALL, TEXT_MUTED);
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(n);
        p.add(l);
        return p;
    }

    
    private JPanel createTopBar() {
        JPanel bar = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(BG_PANEL);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(BORDER_COLOR);
                g2.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
            }
        };
        bar.setPreferredSize(new Dimension(0, 50));
        bar.setOpaque(false);
        bar.setBorder(new EmptyBorder(0, 16, 0, 16));

        
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        left.setOpaque(false);
        JLabel logo = styledLabel(">> Roma Street", FONT_LOGO, ACCENT);
        left.add(logo);
        topLocLabel = styledLabel("", FONT_MONO_SM, ACCENT_SOFT);
        topLocLabel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(232, 69, 69, 50), 1, true),
                new EmptyBorder(2, 10, 2, 10)));
        left.add(topLocLabel);
        bar.add(left, BorderLayout.WEST);

        
        JPanel center = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 0));
        center.setOpaque(false);

        JLabel hpIcon = styledLabel("HP", FONT_HEADER, ACCENT_SOFT);
        center.add(hpIcon);
        topHPBar = createStyledProgressBar(100, ACCENT_GREEN);
        topHPBar.setPreferredSize(new Dimension(120, 8));
        center.add(topHPBar);
        topHPVal = styledLabel("", FONT_MONO_SM, TEXT_DIM);
        center.add(topHPVal);

        center.add(Box.createHorizontalStrut(10));
        JLabel xpIcon = styledLabel("XP", FONT_HEADER, ACCENT_YELLOW);
        center.add(xpIcon);
        topXPVal = styledLabel("", FONT_MONO_SM, TEXT_DIM);
        center.add(topXPVal);

        center.add(Box.createHorizontalStrut(10));
        JLabel lvlIcon = styledLabel("Lv", FONT_BODY, ACCENT_BLUE);
        center.add(lvlIcon);
        topLVLVal = styledLabel("", FONT_MONO_SM, TEXT_DIM);
        center.add(topLVLVal);

        bar.add(center, BorderLayout.CENTER);

        
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        right.setOpaque(false);
        JButton objBtn = createSmallButton("OBJECTIVES");
        objBtn.addActionListener(e -> showObjectivesDialog());
        right.add(objBtn);
        JButton helpBtn = createSmallButton("? Help");
        helpBtn.addActionListener(e -> showHelpDialog());
        right.add(helpBtn);
        bar.add(right, BorderLayout.EAST);

        return bar;
    }

    
    private JPanel createLeftPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(BG_PANEL);
        p.setPreferredSize(new Dimension(260, 0));
        p.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, BORDER_COLOR));

        
        JPanel header = createSectionHeader("MINIMAP");
        p.add(header, BorderLayout.NORTH);

        
        minimapPanel = new MinimapPanel();
        minimapPanel.setPreferredSize(new Dimension(240, 320));
        JPanel mapWrap = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 8));
        mapWrap.setOpaque(false);
        mapWrap.add(minimapPanel);
        p.add(mapWrap, BorderLayout.CENTER);

        
        JPanel legend = new JPanel(new GridLayout(3, 2, 6, 2));
        legend.setOpaque(false);
        legend.setBorder(new EmptyBorder(4, 12, 8, 12));
        legend.add(legendDot(ACCENT, "You"));
        legend.add(legendDot(HP_RED, "Enemies"));
        legend.add(legendDot(ACCENT_YELLOW, "Items"));
        legend.add(legendDot(ACCENT_CYAN, "Visitato"));
        legend.add(legendDot(TEXT_MUTED, "Sconosciuto"));
        legend.add(new JLabel("")); 
        p.add(legend, BorderLayout.SOUTH);

        return p;
    }

    private JPanel legendDot(Color c, String text) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        p.setOpaque(false);
        JPanel dot = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(c);
                g2.fillOval(1, 1, 7, 7);
            }
        };
        dot.setOpaque(false);
        dot.setPreferredSize(new Dimension(9, 9));
        p.add(dot);
        JLabel l = styledLabel(text, new Font("Segoe UI", Font.PLAIN, 10), TEXT_MUTED);
        p.add(l);
        return p;
    }

    
    private JPanel createCenterPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(BG_DARK);

        
        JPanel cardHolder = new JPanel();
        CardLayout cl = new CardLayout();
        cardHolder.setLayout(cl);
        cardHolder.setBackground(BG_DARK);

        
        explorationPanel = createExplorationView();
        cardHolder.add(explorationPanel, "EXPLORE");

        
        combatPanel = createCombatView();
        cardHolder.add(combatPanel, "COMBAT");

        p.add(cardHolder, BorderLayout.CENTER);

        
        JPanel logPanel = createGameLogPanel();
        p.add(logPanel, BorderLayout.SOUTH);

        
        this.mainCardPanel.putClientProperty("centerCards", cl);
        this.mainCardPanel.putClientProperty("centerHolder", cardHolder);

        return p;
    }

    private JPanel createExplorationView() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(BG_DARK);
        p.setBorder(new EmptyBorder(16, 24, 16, 24));

        
        JPanel locHeader = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_PANEL);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(BORDER_COLOR);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
                
                g2.setPaint(new GradientPaint(0, 0, ACCENT, 0, getHeight(), ACCENT_PURPLE));
                g2.fillRoundRect(0, 0, 4, getHeight(), 4, 4);
            }
        };
        locHeader.setOpaque(false);
        locHeader.setBorder(new EmptyBorder(16, 20, 16, 16));
        locHeader.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        locNameLabel = styledLabel("", FONT_TITLE, TEXT);
        locDescLabel = styledLabel("", FONT_BODY, TEXT_DIM);
        locDescLabel.setBorder(new EmptyBorder(4, 0, 0, 8));

        JPanel locTexts = new JPanel();
        locTexts.setLayout(new BoxLayout(locTexts, BoxLayout.Y_AXIS));
        locTexts.setOpaque(false);
        locTexts.add(locNameLabel);
        locTexts.add(locDescLabel);
        locHeader.add(locTexts, BorderLayout.CENTER);

        p.add(locHeader);
        p.add(Box.createVerticalStrut(10));

        
        eventBanner = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, new Color(232, 69, 69, 22), getWidth(), 0,
                        new Color(168, 85, 247, 16)));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(new Color(232, 69, 69, 50));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
            }
        };
        eventBanner.setOpaque(false);
        eventBanner.setBorder(new EmptyBorder(8, 10, 8, 10));
        eventBanner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        JLabel evIcon = styledLabel("!!", FONT_BODY, ACCENT_YELLOW);
        eventBanner.add(evIcon);
        eventLabel = styledLabel("", FONT_BODY, ACCENT_SOFT);
        eventBanner.add(eventLabel);
        eventBanner.setVisible(false);
        p.add(eventBanner);
        p.add(Box.createVerticalStrut(6));

        
        JPanel scrollContent = new JPanel();
        scrollContent.setLayout(new BoxLayout(scrollContent, BoxLayout.Y_AXIS));
        scrollContent.setBackground(BG_DARK);
        
        enemiesSectionPanel = createCollapsibleSection("ENEMIES PRESENT");
        enemiesListPanel = new JPanel();
        enemiesListPanel.setLayout(new BoxLayout(enemiesListPanel, BoxLayout.Y_AXIS));
        enemiesListPanel.setOpaque(false);
        enemiesSectionPanel.add(enemiesListPanel, BorderLayout.CENTER);
        enemiesSectionPanel.setVisible(false);
        scrollContent.add(enemiesSectionPanel);
        scrollContent.add(Box.createVerticalStrut(8));

        
        itemsSectionPanel = createCollapsibleSection("ITEMS ON GROUND");
        itemsListPanel = new JPanel();
        itemsListPanel.setLayout(new BoxLayout(itemsListPanel, BoxLayout.Y_AXIS));
        itemsListPanel.setOpaque(false);
        itemsSectionPanel.add(itemsListPanel, BorderLayout.CENTER);
        itemsSectionPanel.setVisible(false);
        scrollContent.add(itemsSectionPanel);
        scrollContent.add(Box.createVerticalStrut(8));

        
        JPanel exitsSect = createCollapsibleSection("EXITS");
        exitsListPanel = new JPanel(new GridLayout(0, 2, 6, 6));
        exitsListPanel.setOpaque(false);
        exitsListPanel.setBorder(new EmptyBorder(6, 8, 8, 8));
        exitsSect.add(exitsListPanel, BorderLayout.CENTER);
        scrollContent.add(exitsSect);
        scrollContent.add(Box.createVerticalStrut(8));

        
        endingButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        endingButtonPanel.setOpaque(false);
        JButton endingBtn = createAccentButton("Choose the Ending", 14);
        endingBtn.addActionListener(e -> showEndingDialog());
        endingButtonPanel.add(endingBtn);
        endingButtonPanel.setVisible(false);
        scrollContent.add(endingButtonPanel);

        JScrollPane scroll = new JScrollPane(scrollContent);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(14);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        styleScrollBar(scroll);

        
        JPanel scrollWrapper = new JPanel(new BorderLayout());
        scrollWrapper.setOpaque(false);
        scrollWrapper.add(scroll, BorderLayout.CENTER);
        p.add(scrollWrapper);

        return p;
    }

    
    private JPanel createCombatView() {
        JPanel p = new JPanel(new BorderLayout(0, 10));
        p.setBackground(BG_DARK);
        p.setBorder(new EmptyBorder(16, 24, 16, 24));

        
        JPanel arena = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, new Color(232, 69, 69, 10), 0, getHeight(), new Color(0, 0, 0, 0)));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                g2.setColor(BORDER_COLOR);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
            }
        };
        arena.setOpaque(false);
        arena.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 10, 4, 10);

        
        JPanel playerSide = new JPanel();
        playerSide.setLayout(new BoxLayout(playerSide, BoxLayout.Y_AXIS));
        playerSide.setOpaque(false);
        combatPlayerNameLabel = styledLabel("", FONT_SUBTITLE, ACCENT_CYAN);
        combatPlayerNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        playerSide.add(combatPlayerNameLabel);
        playerSide.add(Box.createVerticalStrut(6));
        JLabel playerAvatar = styledLabel("[YOU]", new Font("Segoe UI", Font.BOLD, 28), TEXT);
        playerAvatar.setAlignmentX(Component.CENTER_ALIGNMENT);
        playerSide.add(playerAvatar);
        playerSide.add(Box.createVerticalStrut(6));
        combatPlayerHPBar = createStyledProgressBar(100, ACCENT_GREEN);
        combatPlayerHPBar.setPreferredSize(new Dimension(140, 10));
        combatPlayerHPBar.setMaximumSize(new Dimension(140, 10));
        combatPlayerHPBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        playerSide.add(combatPlayerHPBar);
        playerSide.add(Box.createVerticalStrut(3));
        combatPlayerHPText = styledLabel("", FONT_MONO_SM, TEXT_DIM);
        combatPlayerHPText.setAlignmentX(Component.CENTER_ALIGNMENT);
        playerSide.add(combatPlayerHPText);

        gbc.gridx = 0;
        gbc.gridy = 0;
        arena.add(playerSide, gbc);

        
        JPanel vsPanel = new JPanel();
        vsPanel.setLayout(new BoxLayout(vsPanel, BoxLayout.Y_AXIS));
        vsPanel.setOpaque(false);
        JLabel vsLabel = styledLabel("VS", new Font("Segoe UI", Font.BOLD, 24), ACCENT);
        vsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        vsPanel.add(vsLabel);
        vsPanel.add(Box.createVerticalStrut(6));
        combatTurnLabel = styledLabel("", FONT_SMALL, ACCENT_YELLOW);
        combatTurnLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        vsPanel.add(combatTurnLabel);
        gbc.gridx = 1;
        arena.add(vsPanel, gbc);

        
        JPanel enemySide = new JPanel();
        enemySide.setLayout(new BoxLayout(enemySide, BoxLayout.Y_AXIS));
        enemySide.setOpaque(false);
        combatEnemyNameLabel = styledLabel("", FONT_SUBTITLE, HP_RED);
        combatEnemyNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        enemySide.add(combatEnemyNameLabel);
        enemySide.add(Box.createVerticalStrut(6));
        JLabel enemyAvatar = styledLabel("[!]", new Font("Segoe UI", Font.BOLD, 28), HP_RED);
        enemyAvatar.setAlignmentX(Component.CENTER_ALIGNMENT);
        enemySide.add(enemyAvatar);
        enemySide.add(Box.createVerticalStrut(6));
        combatEnemyHPBar = createStyledProgressBar(100, HP_RED);
        combatEnemyHPBar.setPreferredSize(new Dimension(140, 10));
        combatEnemyHPBar.setMaximumSize(new Dimension(140, 10));
        combatEnemyHPBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        enemySide.add(combatEnemyHPBar);
        enemySide.add(Box.createVerticalStrut(3));
        combatEnemyHPText = styledLabel("", FONT_MONO_SM, TEXT_DIM);
        combatEnemyHPText.setAlignmentX(Component.CENTER_ALIGNMENT);
        enemySide.add(combatEnemyHPText);

        gbc.gridx = 2;
        arena.add(enemySide, gbc);

        p.add(arena, BorderLayout.NORTH);

        
        combatLogArea = new JTextArea();
        combatLogArea.setFont(FONT_MONO);
        combatLogArea.setBackground(BG_PANEL);
        combatLogArea.setForeground(TEXT_DIM);
        combatLogArea.setEditable(false);
        combatLogArea.setLineWrap(true);
        combatLogArea.setWrapStyleWord(true);
        combatLogArea.setBorder(new EmptyBorder(8, 12, 8, 12));
        combatLogArea.setCaretColor(BG_PANEL);
        JScrollPane logScroll = new JScrollPane(combatLogArea);
        logScroll.setPreferredSize(new Dimension(0, 120));
        logScroll.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(0, 0, 0, 0)));
        styleScrollBar(logScroll);
        p.add(logScroll, BorderLayout.CENTER);

        
        combatActionsPanel = new JPanel(new CardLayout());
        combatActionsPanel.setOpaque(false);

        combatMainActions = new JPanel(new GridLayout(1, 5, 6, 0));
        combatMainActions.setOpaque(false);
        combatMainActions.setBorder(new EmptyBorder(8, 0, 0, 0));

        JButton atkBtn = createCombatButton(">> Attack", ACCENT);
        atkBtn.addActionListener(e -> onCombatAttack());
        combatMainActions.add(atkBtn);

        JButton defBtn = createCombatButton("Defend", ACCENT_BLUE);
        defBtn.addActionListener(e -> onCombatDefend());
        combatMainActions.add(defBtn);

        JButton magicBtn = createCombatButton("Magic", ACCENT_PURPLE);
        magicBtn.addActionListener(e -> onCombatMagic());
        combatMainActions.add(magicBtn);

        JButton potBtn = createCombatButton("Potion", ACCENT_GREEN);
        potBtn.addActionListener(e -> onCombatPotion());
        combatMainActions.add(potBtn);

        JButton fleeBtn = createCombatButton("Flee", ACCENT_YELLOW);
        fleeBtn.addActionListener(e -> onCombatFlee());
        combatMainActions.add(fleeBtn);

        combatActionsPanel.add(combatMainActions, "MAIN");

        
        weaponSelectPanel = new JPanel();
        weaponSelectPanel.setLayout(new BoxLayout(weaponSelectPanel, BoxLayout.Y_AXIS));
        weaponSelectPanel.setOpaque(false);
        combatActionsPanel.add(weaponSelectPanel, "WEAPONS");

        
        potionSelectPanel = new JPanel();
        potionSelectPanel.setLayout(new BoxLayout(potionSelectPanel, BoxLayout.Y_AXIS));
        potionSelectPanel.setOpaque(false);
        combatActionsPanel.add(potionSelectPanel, "POTIONS");

        
        spellSelectPanel = new JPanel();
        spellSelectPanel.setLayout(new BoxLayout(spellSelectPanel, BoxLayout.Y_AXIS));
        spellSelectPanel.setOpaque(false);
        combatActionsPanel.add(spellSelectPanel, "SPELLS");

        p.add(combatActionsPanel, BorderLayout.SOUTH);

        return p;
    }

    
    private JPanel createGameLogPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(BG_PANEL);
        p.setPreferredSize(new Dimension(0, 130));
        p.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR));

        JPanel header = createSectionHeader("GAME LOG");
        p.add(header, BorderLayout.NORTH);

        gameLogArea = new JTextArea();
        gameLogArea.setFont(FONT_MONO_SM);
        gameLogArea.setBackground(BG_PANEL);
        gameLogArea.setForeground(TEXT_DIM);
        gameLogArea.setEditable(false);
        gameLogArea.setLineWrap(true);
        gameLogArea.setWrapStyleWord(true);
        gameLogArea.setBorder(new EmptyBorder(4, 12, 4, 12));
        gameLogArea.setCaretColor(BG_PANEL);
        JScrollPane scroll = new JScrollPane(gameLogArea);
        scroll.setBorder(null);
        styleScrollBar(scroll);
        p.add(scroll, BorderLayout.CENTER);

        return p;
    }

    
    private JPanel createRightPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(BG_PANEL);
        p.setPreferredSize(new Dimension(290, 0));
        p.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, BORDER_COLOR));

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(BG_PANEL);

        
        JPanel statsSection = new JPanel(new BorderLayout());
        statsSection.setOpaque(false);
        statsSection.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR));

        JPanel statsHeader = createSectionHeader("STATS");
        statsSection.add(statsHeader, BorderLayout.NORTH);

        JPanel statsGrid = new JPanel(new GridLayout(3, 2, 5, 5));
        statsGrid.setOpaque(false);
        statsGrid.setBorder(new EmptyBorder(8, 8, 8, 8));

        statHP = addStatCard(statsGrid, "Health", ACCENT_SOFT);
        statSTR = addStatCard(statsGrid, "Strength", ACCENT_YELLOW);
        statATK = addStatCard(statsGrid, "Attack", ACCENT);
        statDEF = addStatCard(statsGrid, "Defense", ACCENT_BLUE);
        statXP = addStatCard(statsGrid, "XP", ACCENT_YELLOW);
        statLVL = addStatCard(statsGrid, "Level", ACCENT_PURPLE);

        statsSection.add(statsGrid, BorderLayout.CENTER);

        
        hpBarStat = createStyledProgressBar(100, ACCENT_GREEN);
        hpBarStat.setPreferredSize(new Dimension(0, 5));
        JPanel hpWrap = new JPanel(new BorderLayout());
        hpWrap.setOpaque(false);
        hpWrap.setBorder(new EmptyBorder(0, 10, 6, 10));
        hpWrap.add(hpBarStat, BorderLayout.CENTER);
        statsSection.add(hpWrap, BorderLayout.SOUTH);

        content.add(statsSection);

        
        shieldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        shieldPanel.setOpaque(false);
        shieldPanel.setBorder(new EmptyBorder(4, 10, 4, 10));
        JLabel shIcon = styledLabel("[S]", FONT_BODY, ACCENT_BLUE);
        shieldPanel.add(shIcon);
        shieldLabel = styledLabel("No Shield", FONT_SMALL, TEXT_DIM);
        shieldPanel.add(shieldLabel);
        shieldPanel.setVisible(false);
        content.add(shieldPanel);

        
        JPanel invSection = new JPanel(new BorderLayout());
        invSection.setOpaque(false);

        JPanel invHeader = new JPanel(new BorderLayout());
        invHeader.setOpaque(false);
        invHeader.setBorder(new EmptyBorder(10, 14, 8, 14));
        JLabel invTitle = styledLabel("INVENTORY", FONT_HEADER, TEXT_DIM);
        invHeader.add(invTitle, BorderLayout.WEST);
        weightLabel = styledLabel("0/100", FONT_MONO_SM, TEXT_MUTED);
        invHeader.add(weightLabel, BorderLayout.EAST);
        invSection.add(invHeader, BorderLayout.NORTH);

        inventoryListPanel = new JPanel();
        inventoryListPanel.setLayout(new BoxLayout(inventoryListPanel, BoxLayout.Y_AXIS));
        inventoryListPanel.setBackground(BG_PANEL);
        inventoryListPanel.setBorder(new EmptyBorder(0, 6, 6, 6));

        JScrollPane invScroll = new JScrollPane(inventoryListPanel);
        invScroll.setBorder(null);
        invScroll.setOpaque(false);
        invScroll.getViewport().setOpaque(false);
        styleScrollBar(invScroll);
        invSection.add(invScroll, BorderLayout.CENTER);
        content.add(invSection);

        JScrollPane mainScroll = new JScrollPane(content);
        mainScroll.setBorder(null);
        mainScroll.setOpaque(false);
        mainScroll.getViewport().setOpaque(false);
        mainScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        styleScrollBar(mainScroll);

        p.add(mainScroll, BorderLayout.CENTER);
        return p;
    }

    private JLabel addStatCard(JPanel grid, String label, Color color) {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(BORDER_COLOR);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(6, 8, 6, 8));
        JLabel lbl = styledLabel(label, new Font("Segoe UI", Font.PLAIN, 10), TEXT_MUTED);
        card.add(lbl, BorderLayout.NORTH);
        JLabel val = styledLabel("0", FONT_MONO, color);
        card.add(val, BorderLayout.SOUTH);
        grid.add(card);
        return val;
    }

    
    private JPanel createGameOverPanel() {
        JPanel p = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(new Color(5, 5, 10, 240));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        p.setOpaque(false);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);

        gameOverTitle = styledLabel("GAME OVER", new Font("Segoe UI", Font.BOLD, 42), ACCENT);
        gameOverTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(gameOverTitle);
        content.add(Box.createVerticalStrut(14));
        gameOverText = styledLabel("", FONT_BODY, TEXT_DIM);
        gameOverText.setAlignmentX(Component.CENTER_ALIGNMENT);
        gameOverText.setHorizontalAlignment(SwingConstants.CENTER);
        content.add(gameOverText);
        content.add(Box.createVerticalStrut(30));
        JButton replayBtn = createAccentButton("GIOCA ANCORA", 16);
        replayBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        replayBtn.addActionListener(e -> restartGame());
        content.add(replayBtn);

        p.add(content);
        return p;
    }

    

    private void showIntro() {
        state = GameState.INTRO;
        mainCardLayout.show(mainCardPanel, "INTRO");
    }

    private void startGame() {
        if (state != GameState.INTRO)
            return;
        World = WorldInitializer.initializeWorld();
        protagonist = World.getProtagonist();
        visitedLocations.clear();
        defeatedEnemies.clear();
        previousLocation = null;
        endingEpilogue = "";
        chargesFire = 2;
        chargesLightning = 1;
        chargesIce = 1;
        if (gameLogArea != null)
            gameLogArea.setText("");

        state = GameState.EXPLORING;
        mainCardLayout.show(mainCardPanel, "GAME");
        switchCenterView("EXPLORE");
        updateAll();
        addLog("Adventure started. You are " + protagonist.getName() + ".");
    }

    private void restartGame() {
        state = GameState.INTRO;
        mainCardLayout.show(mainCardPanel, "INTRO");
    }

    private void switchCenterView(String name) {
        CardLayout cl = (CardLayout) mainCardPanel.getClientProperty("centerCards");
        JPanel holder = (JPanel) mainCardPanel.getClientProperty("centerHolder");
        if (cl != null && holder != null) {
            cl.show(holder, name);
        }
    }

    private void updateAll() {
        if (World == null)
            return;
        Location loc = World.getCurrentPosition();
        visitedLocations.add(loc.getName());
        updateLocationView(loc);
        updateStatsPanel();
        updateInventoryPanel();
        updateTopBar();
        if (minimapPanel != null)
            minimapPanel.repaint();
    }

    private void updateLocationView(Location loc) {
        
        String icon = LOCATION_ICONS.getOrDefault(loc.getName(), "[?]");
        locNameLabel.setText(icon + "  " + loc.getName().toUpperCase());
        locDescLabel.setText(loc.getDescription());

        
        topLocLabel.setText(loc.getName());

        
        showEventIfNew(loc);

        
        endingButtonPanel.setVisible("Mix Market".equals(loc.getName()));
        
        List<GameCharacter> enemiesRaw = loc.getEnemies(protagonist);
        List<GameCharacter> enemies = new ArrayList<>();
        for (GameCharacter enemy : enemiesRaw) {
            if (!defeatedEnemies.contains(enemy.getName())) {
                enemies.add(enemy);
            }
        }
        enemiesListPanel.removeAll();
        if (!enemies.isEmpty()) {
            enemiesSectionPanel.setVisible(true);
            for (GameCharacter enemy : enemies) {
                enemiesListPanel.add(createEnemyCard(enemy));
                enemiesListPanel.add(Box.createVerticalStrut(4));
            }
        } else {
            enemiesSectionPanel.setVisible(false);
        }

        
        List<Item> items = loc.getItems();
        itemsListPanel.removeAll();
        if (!items.isEmpty()) {
            itemsSectionPanel.setVisible(true);
            for (int i = 0; i < items.size(); i++) {
                itemsListPanel.add(createItemCard(items.get(i), i));
                itemsListPanel.add(Box.createVerticalStrut(3));
            }
        } else {
            itemsSectionPanel.setVisible(false);
        }

        
        List<LockableContainer> containers = loc.getLockableContainers();
        if (!containers.isEmpty()) {
            for (int i = 0; i < containers.size(); i++) {
                LockableContainer c = containers.get(i);
                final int cidx = i;
                JPanel cCard = new JPanel(new BorderLayout(6, 0)) {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        Graphics2D g2 = (Graphics2D) g;
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(BG_CARD);
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                        g2.setColor(BORDER_COLOR);
                        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                        
                        g2.setColor(ACCENT_PURPLE);
                        g2.fillRoundRect(0, 0, 3, getHeight(), 3, 3);
                    }
                };
                cCard.setOpaque(false);
                cCard.setBorder(new EmptyBorder(8, 10, 8, 8));
                cCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));

                JPanel cInfo = new JPanel();
                cInfo.setLayout(new BoxLayout(cInfo, BoxLayout.Y_AXIS));
                cInfo.setOpaque(false);
                String status = c.isLocked() ? "[LOCKED]" : "[OPEN]";
                String lock = c.isRequiresKey() ? " (Key)" : "";
                JLabel cName = styledLabel(status + lock + " " + c.getName(), new Font("Segoe UI", Font.BOLD, 12), TEXT);
                cInfo.add(cName);
                cCard.add(cInfo, BorderLayout.CENTER);

                JPanel cActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
                cActions.setOpaque(false);
                if (c.isLocked()) {
                    JButton openBtn = createTinyButton("Open", ACCENT_PURPLE);
                    openBtn.addActionListener(ev -> openContainer(cidx));
                    cActions.add(openBtn);
                } else {
                    JButton lootBtn = createTinyButton("Take", ACCENT_GREEN);
                    lootBtn.addActionListener(ev -> lootContainer(cidx));
                    cActions.add(lootBtn);
                }
                cCard.add(cActions, BorderLayout.EAST);

                itemsListPanel.add(cCard);
                itemsListPanel.add(Box.createVerticalStrut(3));
                itemsSectionPanel.setVisible(true);
            }
        }

        
        exitsListPanel.removeAll();
        LinkedHashMap<String, Location> usciteUniche = new LinkedHashMap<>();
        for (Map.Entry<String, Location> e : loc.getConnections().entrySet()) {
            if (!usciteUniche.containsValue(e.getValue())) {
                usciteUniche.put(e.getKey(), e.getValue());
            }
        }
        for (Map.Entry<String, Location> e : usciteUniche.entrySet()) {
            String cmd = e.getKey();
            Location dest = e.getValue();
            JButton btn = createExitButton(cmd, dest.getName());
            btn.addActionListener(ev -> moveToLocation(cmd));
            exitsListPanel.add(btn);
        }

        explorationPanel.revalidate();
        explorationPanel.repaint();
    }

    private void showEventIfNew(Location loc) {
        boolean newVisit = visitedLocations.size() == 1 || !visitedLocations.contains(loc.getName());
        
        String msg = null;
        switch (loc.getName()) {
            case "Roma Street - Brawl":
                if (visitedLocations.add(loc.getName() + "_event")) {
                    msg = "The brawl erupts: Luca Vassena targets you.";
                }
                break;
            case "Underground Parking":
                if (visitedLocations.add(loc.getName() + "_event")) {
                    msg = "The Junkie and The Wall rule here.";
                }
                break;
            case "Building Rooftop":
                if (visitedLocations.add(loc.getName() + "_event")) {
                    msg = "The Bangla controls everything from above.";
                }
                break;
            case "Mix Market":
                if (visitedLocations.add(loc.getName() + "_event")) {
                    msg = "Point of no return: choose your ending here.";
                }
                break;
        }
        if (msg != null) {
            eventLabel.setText(msg);
            eventBanner.setVisible(true);
            addLog("[Event] " + msg);
        } else {
            eventBanner.setVisible(false);
        }
    }

    private void moveToLocation(String direction) {
        if (state != GameState.EXPLORING)
            return;
        Location current = World.getCurrentPosition();
        Location dest = current.getConnection(direction);
        if (dest == null)
            return;

        
        String curName = current.getName();
        boolean blocked = false;
        if ("Roma Street - Brawl".equals(curName) && !defeatedEnemies.contains("Luca Vassena")) {
            
            if (!"back".equals(direction)) {
                addLog("You must defeat Luca Vassena before proceeding!");
                blocked = true;
            }
        } else if ("Underground Parking".equals(curName)
                && (!defeatedEnemies.contains("The Junkie") || !defeatedEnemies.contains("The Wall"))) {
            if (!"back".equals(direction)) {
                addLog("You must defeat The Junkie and The Wall before proceeding!");
                blocked = true;
            }
        } else if ("Building Rooftop".equals(curName) && !defeatedEnemies.contains("The Bangla")) {
            if (!"down".equals(direction) && !"descend".equals(direction)) {
                
                addLog("You must defeat The Bangla before proceeding!");
                blocked = true;
            }
        }

        if (blocked) {
            updateAll();
            return;
        }

        Location prev = current;
        if (World.move(direction)) {
            previousLocation = prev;
            Location newLoc = World.getCurrentPosition();
            addLog("You move toward " + newLoc.getName() + ".");
            updateAll();
        }
    }

    private void pickUpItem(int idx) {
        if (state != GameState.EXPLORING)
            return;
        Location loc = World.getCurrentPosition();
        if (idx < 0 || idx >= loc.getItems().size())
            return;
        Item item = loc.getItems().get(idx);
        if (!item.isTransportable()) {
            addLog("Object not transportable.");
            return;
        }
        if (!protagonist.addItem(item)) {
            addLog("Inventory full or too heavy.");
            return;
        }
        loc.removeItem(item);
        addLog("Picked up: " + item.getName());
        updateAll();
    }

    private void openContainer(int idx) {
        if (state != GameState.EXPLORING) return;
        Location loc = World.getCurrentPosition();
        List<LockableContainer> containers = loc.getLockableContainers();
        if (idx < 0 || idx >= containers.size()) return;
        LockableContainer c = containers.get(idx);

        if (!c.isLocked()) {
            addLog(c.getName() + " is already open.");
            updateAll();
            return;
        }

        if (c.isRequiresKey()) {
            
            if (protagonist.hasItem("Electronic Key")) {
                c.openWithKey();
                addLog("You use the Electronic Key to open " + c.getName() + "!");
            } else {
                addLog(c.getName() + " is locked. You need the Electronic Key.");
            }
        } else {
            c.openWithKey(); 
            addLog("Open " + c.getName() + "!");
        }
        updateAll();
    }

    private void lootContainer(int idx) {
        if (state != GameState.EXPLORING) return;
        Location loc = World.getCurrentPosition();
        List<LockableContainer> containers = loc.getLockableContainers();
        if (idx < 0 || idx >= containers.size()) return;
        LockableContainer c = containers.get(idx);

        if (c.isLocked()) {
            addLog(c.getName() + " is locked!");
            updateAll();
            return;
        }

        Item loot = c.getContainedItem();
        if (loot == null) {
            addLog(c.getName() + " is empty.");
        } else {
            if (protagonist.addItem(loot)) {
                c.removeItem();
                addLog("Taken from " + c.getName() + ": " + loot.getName() + "!");
            } else {
                addLog("Inventory full! Cannot take " + loot.getName() + ".");
            }
        }
        updateAll();
    }

    private void dropItem(int idx) {
        if (idx < 0 || idx >= protagonist.getInventory().size())
            return;
        Location loc = World.getCurrentPosition();
        Transportable item = protagonist.getInventory().get(idx);
        protagonist.removeItem(item);
        if (item instanceof Item) {
            loc.addItem((Item) item);
        }
        addLog("Dropped: " + item.getName());
        updateAll();
    }

    private void useItem(int idx) {
        if (idx < 0 || idx >= protagonist.getInventory().size())
            return;
        Transportable item = protagonist.getInventory().get(idx);

        if (item instanceof PoisonPotion) {
            addLog("Poison potions are used in combat.");
            return;
        }
        if (item instanceof Potion) {
            boolean usedPotion = protagonist.usePotion(idx);
            addLog(usedPotion ? "Potion used!" : "Can't use it now.");
            updateAll();
            return;
        }
        if (item instanceof Item) {
            String itemName = item.getName().toLowerCase();
            if (itemName.contains("kebab")) {
                protagonist.setHealth(protagonist.getHealth() + 20);
                protagonist.removeItem(item);
                addLog("Recovered 20 HP.");
            } else if (itemName.contains("can")) {
                protagonist.setHealth(protagonist.getMaxHealth());
                protagonist.gainExperience(10);
                protagonist.removeItem(item);
                addLog("Health maxed and +10 XP!");
            } else if (itemName.contains("kit") || itemName.contains("bandage")) {
                protagonist.setHealth(protagonist.getHealth() + 15);
                protagonist.removeItem(item);
                addLog("Recovered 15 HP.");
            } else {
                addLog("No effect.");
            }
            updateAll();
        }
    }

    private void equipItem(int idx) {
        if (idx < 0 || idx >= protagonist.getInventory().size())
            return;
        Transportable item = protagonist.getInventory().get(idx);
        if (!(item instanceof Shield)) {
            addLog("You can equip shields only.");
            return;
        }
        if (!protagonist.equipShield((Shield) item)) {
            addLog("You already have a shield equipped.");
            return;
        }
        protagonist.removeItem(item);
        addLog("Shield equipped: " + item.getName());
        updateAll();
    }

    

    private void startCombat(GameCharacter target) {
        if (state != GameState.EXPLORING)
            return;
        
        if (!target.isAlive() || defeatedEnemies.contains(target.getName()))
            return;
        Location loc = World.getCurrentPosition();
        Enemy enemy = new Enemy(target.getName(), target.getHealth(), target.getAttack());
        enemy.setHealth(target.getHealth());

        combatEnemy = enemy;
        combatLocation = loc;
        playerTurn = true; 

        state = GameState.COMBAT_PLAYER;
        switchCenterView("COMBAT");
        combatLogArea.setText("");
        addCombatLog("You attack first!");

        
        burnTurns = 0;
        freezeTurns = 0;

        setCombatActionsEnabled(true);
        ((CardLayout) combatActionsPanel.getLayout()).show(combatActionsPanel, "MAIN");
        updateCombatUI();
    }

    private void updateCombatUI() {
        if (combatEnemy == null)
            return;
        combatPlayerNameLabel.setText(protagonist.getName());
        combatEnemyNameLabel.setText(combatEnemy.getName());

        int playerHPPct = (int) ((double) protagonist.getHealth() / protagonist.getMaxHealth() * 100);
        combatPlayerHPBar.setValue(playerHPPct);
        combatPlayerHPBar.setForeground(hpColor(playerHPPct));
        combatPlayerHPText.setText(protagonist.getHealth() + "/" + protagonist.getMaxHealth());

        int maxEnemHP = getEnemyMaxHP();
        int enemHPPct = combatEnemy.getHealth() > 0
                ? Math.max(1, (combatEnemy.getHealth() * 100 / Math.max(1, maxEnemHP)))
                : 0;
        combatEnemyHPBar.setValue(Math.min(100, enemHPPct));
        combatEnemyHPBar.setForeground(HP_RED);
        combatEnemyHPText.setText(combatEnemy.getHealth() + "/" + maxEnemHP);

        combatTurnLabel.setText(playerTurn ? ">> YOUR TURN <<" : ">> ENEMY'S TURN <<");
        combatPanel.revalidate();
        combatPanel.repaint();
        updateTopBar();
    }

    
    private int getEnemyMaxHP() {
        String name = combatEnemy.getName();
        switch (name) {
            case "Luca Vassena":
                return 95;
            case "The Junkie":
                return 140;
            case "The Wall":
                return 150;
            case "The Bangla":
                return 130;
            default:
                return combatEnemy.getHealth();
        }
    }

    private void setCombatActionsEnabled(boolean enabled) {
        for (Component c : combatMainActions.getComponents()) {
            c.setEnabled(enabled);
        }
    }

    private void onCombatAttack() {
        if (state != GameState.COMBAT_PLAYER)
            return;
        
        weaponSelectPanel.removeAll();
        JLabel wTitle = styledLabel("CHOOSE WEAPON", FONT_HEADER, TEXT_DIM);
        wTitle.setBorder(new EmptyBorder(4, 8, 6, 8));
        weaponSelectPanel.add(wTitle);

        boolean hasWeapons = false;
        for (int i = 0; i < protagonist.getInventory().size(); i++) {
            Transportable t = protagonist.getInventory().get(i);
            if (t instanceof Weapon) {
                hasWeapons = true;
                Weapon a = (Weapon) t;
                final int idx = i;
                JButton wb = createOptionButton(
                        a.getName() + (a.getWeaponState() ? "" : " [BROKEN]"),
                        "Damage: " + (int) (a.getDamage() * 0.6) + "-" + a.getDamage(),
                        ACCENT);
                wb.addActionListener(e -> attackWithWeapon(idx));
                weaponSelectPanel.add(wb);
                weaponSelectPanel.add(Box.createVerticalStrut(3));
            }
        }

        JButton fistBtn = createOptionButton("Fists", "Damage: " + protagonist.getAttack() / 2, ACCENT_YELLOW);
        fistBtn.addActionListener(e -> attackWithFists());
        weaponSelectPanel.add(fistBtn);

        JButton cancelBtn = createOptionButton("X Cancel", "", TEXT_MUTED);
        cancelBtn.addActionListener(e -> showMainCombatActions());
        weaponSelectPanel.add(Box.createVerticalStrut(4));
        weaponSelectPanel.add(cancelBtn);

        state = GameState.COMBAT_WEAPON;
        ((CardLayout) combatActionsPanel.getLayout()).show(combatActionsPanel, "WEAPONS");
    }

    private void attackWithWeapon(int idx) {
        if (idx < 0 || idx >= protagonist.getInventory().size())
            return;
        Transportable t = protagonist.getInventory().get(idx);
        if (!(t instanceof Weapon)) {
            attackWithFists();
            return;
        }

        Weapon Weapon = (Weapon) t;
        if (!Weapon.getWeaponState()) {
            addCombatLog("The weapon is broken! You use your fists.");
            int damage = protagonist.getAttack() / 2;
            combatEnemy.takeDamage(damage);
            addCombatLog("Strike with bare fists for " + damage + " damage! [Enemy: " + combatEnemy.getHealth()
                    + " HP]");
        } else if (protagonist.getStrength() < Weapon.getMinStrength()) {
            addCombatLog("Not enough strength! Using fists.");
            int damage = protagonist.getAttack() / 2;
            combatEnemy.takeDamage(damage);
            addCombatLog("Strike for " + damage + " damage! [Enemy: " + combatEnemy.getHealth() + " HP]");
        } else {
            int damage = protagonist.attackEnemy(combatEnemy, Weapon);
            addCombatLog("Strike with " + Weapon.getName() + " for " + damage + " damage! [Enemy: "
                    + combatEnemy.getHealth() + " HP]");
        }
        protagonist.decrementPotionTurns();
        endPlayerTurn();
    }

    private void attackWithFists() {
        int damage = protagonist.getAttack() / 2;
        combatEnemy.takeDamage(damage);
        addCombatLog(
                "Strike with bare fists for " + damage + " damage! [Enemy: " + combatEnemy.getHealth() + " HP]");
        protagonist.decrementPotionTurns();
        endPlayerTurn();
    }

    private void onCombatDefend() {
        if (state != GameState.COMBAT_PLAYER)
            return;
        protagonist.setDefenseMode(true);
        addCombatLog("You take a defensive stance!");
        protagonist.decrementPotionTurns();
        endPlayerTurn();
    }

    

    private void onCombatMagic() {
        if (state != GameState.COMBAT_PLAYER)
            return;
        spellSelectPanel.removeAll();
        JLabel sTitle = styledLabel("CHOOSE SPELL", FONT_HEADER, TEXT_DIM);
        sTitle.setBorder(new EmptyBorder(4, 8, 6, 8));
        spellSelectPanel.add(sTitle);

        if (chargesFire > 0) {
            JButton fireBtn = createOptionButton("Fireball (" + chargesFire + "x)", "30 damage + 5/turn for 5 turns", ACCENT);
            fireBtn.addActionListener(e -> castSpell("Fireball"));
            spellSelectPanel.add(fireBtn);
            spellSelectPanel.add(Box.createVerticalStrut(3));
        } else {
            JButton fireBtn = createOptionButton("Fireball [EXHAUSTED]", "", TEXT_MUTED);
            fireBtn.setEnabled(false);
            spellSelectPanel.add(fireBtn);
            spellSelectPanel.add(Box.createVerticalStrut(3));
        }

        if (chargesLightning > 0) {
            JButton lightBtn = createOptionButton("Sky Lightning (" + chargesLightning + "x)", "25 instant damage", ACCENT_YELLOW);
            lightBtn.addActionListener(e -> castSpell("Sky Lightning"));
            spellSelectPanel.add(lightBtn);
            spellSelectPanel.add(Box.createVerticalStrut(3));
        } else {
            JButton lightBtn = createOptionButton("Sky Lightning [EXHAUSTED]", "", TEXT_MUTED);
            lightBtn.setEnabled(false);
            spellSelectPanel.add(lightBtn);
            spellSelectPanel.add(Box.createVerticalStrut(3));
        }

        if (chargesIce > 0) {
            JButton iceBtn = createOptionButton("Frost Wave (" + chargesIce + "x)", "Freeze + 3/turn for 5 turns", ACCENT_CYAN);
            iceBtn.addActionListener(e -> castSpell("Frost Wave"));
            spellSelectPanel.add(iceBtn);
            spellSelectPanel.add(Box.createVerticalStrut(4));
        } else {
            JButton iceBtn = createOptionButton("Frost Wave [EXHAUSTED]", "", TEXT_MUTED);
            iceBtn.setEnabled(false);
            spellSelectPanel.add(iceBtn);
            spellSelectPanel.add(Box.createVerticalStrut(4));
        }

        JButton cancelBtn = createOptionButton("X Cancel", "", TEXT_MUTED);
        cancelBtn.addActionListener(e -> showMainCombatActions());
        spellSelectPanel.add(cancelBtn);

        state = GameState.COMBAT_PLAYER;
        ((CardLayout) combatActionsPanel.getLayout()).show(combatActionsPanel, "SPELLS");
    }

    private void castSpell(String spellName) {
        switch (spellName) {
            case "Fireball":
                if (chargesFire <= 0) { addCombatLog("Fireball exhausted!"); return; }
                chargesFire--;
                combatEnemy.takeDamage(30);
                burnTurns = 5;
                addCombatLog("** Fireball! ** 30 damage! The enemy burns! [Enemy: "
                        + combatEnemy.getHealth() + " HP] (" + chargesFire + " remaining)");
                break;
            case "Sky Lightning":
                if (chargesLightning <= 0) { addCombatLog("Sky Lightning exhausted!"); return; }
                chargesLightning--;
                combatEnemy.takeDamage(25);
                addCombatLog("** Sky Lightning! ** 25 damage! [Enemy: " + combatEnemy.getHealth() + " HP]");
                break;
            case "Frost Wave":
                if (chargesIce <= 0) { addCombatLog("Frost Wave exhausted!"); return; }
                chargesIce--;
                freezeTurns = 5;
        addCombatLog("** Frost Wave! ** The enemy is frozen for 5 turns!");
                break;
        }
        protagonist.decrementPotionTurns();
        endPlayerTurn();
    }

    private void onCombatPotion() {
        if (state != GameState.COMBAT_PLAYER)
            return;
        potionSelectPanel.removeAll();
        JLabel pTitle = styledLabel("CHOOSE POTION", FONT_HEADER, TEXT_DIM);
        pTitle.setBorder(new EmptyBorder(4, 8, 6, 8));
        potionSelectPanel.add(pTitle);

        boolean found = false;
        for (int i = 0; i < protagonist.getInventory().size(); i++) {
            Transportable t = protagonist.getInventory().get(i);
            if (t instanceof Potion) {
                found = true;
                final int idx = i;
                String info = "";
                if (t instanceof Heal)
                    info = "Heal: +" + (((Heal) t).getHealValue() * 3) + " HP";
                else if (t instanceof PowerPotion)
                    info = "PowerPotion: +" + ((PowerPotion) t).getRestoreValue();
                else if (t instanceof PoisonPotion)
                    info = "PoisonPotion: " + ((PoisonPotion) t).getDamage() + " danni";
                JButton pb = createOptionButton(t.getName(), info, ACCENT_GREEN);
                pb.addActionListener(e -> usePotionInCombat(idx));
                potionSelectPanel.add(pb);
                potionSelectPanel.add(Box.createVerticalStrut(3));
            }
        }

        if (!found) {
            addCombatLog("You have no potions!");
            return;
        }

        JButton cancelBtn = createOptionButton("X Cancel", "", TEXT_MUTED);
        cancelBtn.addActionListener(e -> showMainCombatActions());
        potionSelectPanel.add(Box.createVerticalStrut(4));
        potionSelectPanel.add(cancelBtn);

        state = GameState.COMBAT_POTION;
        ((CardLayout) combatActionsPanel.getLayout()).show(combatActionsPanel, "POTIONS");
    }

    private void usePotionInCombat(int idx) {
        if (idx < 0 || idx >= protagonist.getInventory().size())
            return;
        Transportable item = protagonist.getInventory().get(idx);

        if (item instanceof PoisonPotion) {
            PoisonPotion PoisonPotion = (PoisonPotion) item;
            combatEnemy.takeDamage(PoisonPotion.getDamage());
            protagonist.removeItem(PoisonPotion);
            addCombatLog("Lanci " + PoisonPotion.getName() + " e infliggi " + PoisonPotion.getDamage() + " damage!");
        } else if (protagonist.usePotion(idx)) {
            addCombatLog("Potion used!");
        } else {
            addCombatLog("Can't use it now.");
        }
        protagonist.decrementPotionTurns();
        endPlayerTurn();
    }

    private void onCombatFlee() {
        if (state != GameState.COMBAT_PLAYER)
            return;
        if (protagonist.flee()) {
            addCombatLog("You fled!");
            endCombat(false);
        } else {
            addCombatLog("Couldn't escape!");
            protagonist.decrementPotionTurns();
            endPlayerTurn();
        }
    }

    private void showMainCombatActions() {
        state = GameState.COMBAT_PLAYER;
        setCombatActionsEnabled(true);
        ((CardLayout) combatActionsPanel.getLayout()).show(combatActionsPanel, "MAIN");
    }

    private void endPlayerTurn() {
        showMainCombatActions();
        if (!combatEnemy.isAlive()) {
            addCombatLog("You defeated " + combatEnemy.getName() + "!");
            endCombat(true);
            return;
        }
        playerTurn = false;
        state = GameState.COMBAT_ENEMY;
        setCombatActionsEnabled(false);
        updateCombatUI();
        scheduleEnemyTurn();
    }

    private void scheduleEnemyTurn() {
        javax.swing.Timer timer = new javax.swing.Timer(900, e -> {
            executeEnemyTurn();
            ((javax.swing.Timer) e.getSource()).stop();
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void executeEnemyTurn() {
        if (combatEnemy == null || !combatEnemy.isAlive()) {
            playerTurn = true;
            state = GameState.COMBAT_PLAYER;
            setCombatActionsEnabled(true);
            updateCombatUI();
            return;
        }

        
        if (burnTurns > 0) {
            combatEnemy.takeDamage(5);
            burnTurns--;
            addCombatLog("Fire burns " + combatEnemy.getName() + "! -5 HP ["
                    + combatEnemy.getHealth() + " HP] (" + burnTurns + " turns left)");
            if (!combatEnemy.isAlive()) {
                addCombatLog(combatEnemy.getName() + " burned to death!");
                endCombat(true);
                return;
            }
        }
        if (freezeTurns > 0) {
            combatEnemy.takeDamage(3);
            freezeTurns--;
            addCombatLog(combatEnemy.getName() + " is frozen! -3 HP [" + combatEnemy.getHealth()
                    + " HP] (" + freezeTurns + " turns left)");
            if (!combatEnemy.isAlive()) {
                addCombatLog(combatEnemy.getName() + " froze to death!");
                endCombat(true);
                return;
            }
            
            addCombatLog(combatEnemy.getName() + " can't move!");
            playerTurn = true;
            state = GameState.COMBAT_PLAYER;
            setCombatActionsEnabled(true);
            updateCombatUI();
            return;
        }

        Random rng = new Random();

        
        if (combatEnemy.getHealth() < 30 && rng.nextInt(100) < 30) {
            addCombatLog(combatEnemy.getName() + " tries to flee... but can't make it!");
        }

        int damage = combatEnemy.attack();
        addCombatLog(combatEnemy.getName() + " attacks you for " + damage + " damage!");
        protagonist.takeDamage(damage);

        if (!protagonist.isAlive()) {
            int reviveHP = Math.max(10, (int) (protagonist.getMaxHealth() * 0.15));
            protagonist.setAlive(true);
            protagonist.setHealth(reviveHP);
            addCombatLog("About to collapse... but you find the strength to stand! (" + reviveHP + " HP)");
            addLog("[!] Second chance! You stand back up with " + reviveHP + " HP.");
        }

        playerTurn = true;
        state = GameState.COMBAT_PLAYER;
        setCombatActionsEnabled(true);
        updateCombatUI();
    }

    private void endCombat(boolean victory) {
        if (victory && combatEnemy != null) {
            combatEnemy.die(combatLocation);
            combatEnemy.transferExperience(protagonist);
            
            GameCharacter defeated = null;
            for (GameCharacter p : combatLocation.getCharacters()) {
                if (p != protagonist && p.getName().equals(combatEnemy.getName())) {
                    defeated = p;
                    break;
                }
            }
            if (defeated != null) {
                defeated.setAlive(false);
                defeated.setHealth(0);
                combatLocation.removeCharacter(defeated);
            }
            defeatedEnemies.add(combatEnemy.getName());
            
            String bossName = combatEnemy.getName();
            Item loot = null;
            if ("Luca Vassena".equals(bossName)) {
                loot = new Item("Mysterious Can", "The old man said: 'If things go bad, drink it.'", 1, 0, true);
            } else if ("The Junkie".equals(bossName)) {
                loot = new Item("Electronic Key", "Opens blocked passages and improvised safes.", 2, 0, true);
            } else if ("The Wall".equals(bossName)) {
                loot = new Item("Contact Dossier", "Names, debts, deliveries. Worth more than a gun.", 3, 0,
                        true);
            } else if ("The Bangla".equals(bossName)) {
                loot = new Item("Intercity Pass", "Open ticket to leave the city tonight.", 1, 0, true);
            }
            if (loot != null) {
                if (protagonist.addItem(loot)) {
                    addLog("Loot: " + loot.getName() + " added to inventory!");
                } else {
                    combatLocation.addItem(loot);
                    addLog("Loot: " + loot.getName() + " dropped on ground (inventory full).");
                }
            }

            addLog("You defeated " + combatEnemy.getName() + "!");
        }

        combatEnemy = null;
        combatLocation = null;

        
        state = GameState.EXPLORING;
        switchCenterView("EXPLORE");
        setCombatActionsEnabled(true);
        updateAll();
    }

    private void showGameOver(boolean victory) {
        state = GameState.GAME_OVER;
        if (victory) {
            gameOverTitle.setText("FINE AVVENTURA");
            gameOverTitle.setForeground(ACCENT_YELLOW);
            gameOverText.setText("<html><center>" + endingEpilogue + "</center></html>");
        } else {
            gameOverTitle.setText("GAME OVER");
            gameOverTitle.setForeground(ACCENT);
            gameOverText.setText(
                    "<html><center>" + protagonist.getName() + " died in the street war.</center></html>");
        }
        mainCardLayout.show(mainCardPanel, "GAMEOVER");
    }

    

    private void showEndingDialog() {
        if (!"Mix Market".equals(World.getCurrentPosition().getName()))
            return;
        JDialog dialog = new JDialog(this, "Choose the Ending", true);
        dialog.setSize(500, 380);
        dialog.setLocationRelativeTo(this);
        dialog.setUndecorated(true);
        dialog.getRootPane().setBorder(new LineBorder(BORDER_COLOR, 1, true));

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(BG_PANEL);
        content.setBorder(new EmptyBorder(20, 24, 20, 24));

        JLabel title = styledLabel("CHOOSE YOUR ENDING", FONT_SUBTITLE, TEXT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(title);
        content.add(Box.createVerticalStrut(20));

        JButton justiceBtn = createEndingButton("Justice", "Bring down the criminal network", ACCENT_BLUE);
        justiceBtn.addActionListener(e -> {
            dialog.dispose();
            chooseEnding(1);
        });
        content.add(justiceBtn);
        content.add(Box.createVerticalStrut(8));

        JButton powerBtn = createEndingButton("Power", "Take control of Roma Street", ACCENT_YELLOW);
        powerBtn.addActionListener(e -> {
            dialog.dispose();
            chooseEnding(2);
        });
        content.add(powerBtn);
        content.add(Box.createVerticalStrut(8));

        JButton escapeBtn = createEndingButton("Escape", "Leave the city and survive", ACCENT_GREEN);
        escapeBtn.addActionListener(e -> {
            dialog.dispose();
            chooseEnding(3);
        });
        content.add(escapeBtn);
        content.add(Box.createVerticalStrut(14));

        JButton cancelBtn = createSmallButton("Cancel");
        cancelBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        cancelBtn.addActionListener(e -> dialog.dispose());
        content.add(cancelBtn);

        dialog.setContentPane(content);
        dialog.setVisible(true);
    }

    private void chooseEnding(int choice) {
        int bossesDefeated = countDefeatedBosses();
        boolean hasKey = protagonist.hasItem("Electronic Key");
        boolean hasDossier = protagonist.hasItem("Contact Dossier");
        boolean hasCan = protagonist.hasItem("Mysterious Can");
        boolean hasPass = protagonist.hasItem("Intercity Pass");

        switch (choice) {
            case 1:
                endingEpilogue = (bossesDefeated >= 3 && hasKey && hasDossier)
                        ? "Finale Giustizia: la rete criminale crolla."
                        : "Finale Fallito: mancano prove decisive.";
                break;
            case 2:
                endingEpilogue = (bossesDefeated >= 3 && hasCan)
                        ? "Finale Potere: prendi il controllo di Roma Street."
                        : "Finale Ambizione Spezzata: non hai abbastanza potere.";
                break;
            case 3:
                endingEpilogue = hasPass
                        ? "Finale Fuga: lasci la citta' e sopravvivi."
                        : "Finale Strada Chiusa: senza pass resti intrappolato.";
                break;
        }
        showGameOver(true);
    }

    private int countDefeatedBosses() {
        int c = 0;
        for (String boss : MAIN_BOSSES) {
            if (defeatedEnemies.contains(boss))
                c++;
        }
        return c;
    }

    

    private void showObjectivesDialog() {
        JDialog dialog = new JDialog(this, "OBJECTIVES", true);
        dialog.setSize(420, 450);
        dialog.setLocationRelativeTo(this);
        dialog.setUndecorated(true);
        dialog.getRootPane().setBorder(new LineBorder(BORDER_COLOR, 1, true));

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(BG_PANEL);
        content.setBorder(new EmptyBorder(16, 20, 16, 20));

        JLabel title = styledLabel("OBJECTIVES", FONT_SUBTITLE, TEXT);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(title);
        content.add(Box.createVerticalStrut(14));

        JLabel bossTitle = styledLabel("Main Bosses:", FONT_HEADER, TEXT_DIM);
        bossTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(bossTitle);
        content.add(Box.createVerticalStrut(6));

        for (String boss : MAIN_BOSSES) {
            boolean done = defeatedEnemies.contains(boss);
            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 2));
            row.setOpaque(false);
            JLabel check = styledLabel(done ? "[X]" : "[ ]", FONT_BODY, done ? ACCENT_GREEN : TEXT_MUTED);
            row.add(check);
            JLabel name = styledLabel(boss, FONT_BODY, done ? TEXT : TEXT_DIM);
            row.add(name);
            row.setAlignmentX(Component.LEFT_ALIGNMENT);
            content.add(row);
        }

        content.add(Box.createVerticalStrut(12));
        JLabel itemsTitle = styledLabel("Key items:", FONT_HEADER, TEXT_DIM);
        itemsTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(itemsTitle);
        content.add(Box.createVerticalStrut(6));

        String[] keyItems = { "Electronic Key", "Contact Dossier", "Intercity Pass", "Mysterious Can" };
        for (String ki : keyItems) {
            boolean has = protagonist != null && protagonist.hasItem(ki);
            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 2));
            row.setOpaque(false);
            JLabel check = styledLabel(has ? "[X]" : "[ ]", FONT_BODY, has ? ACCENT_GREEN : TEXT_MUTED);
            row.add(check);
            JLabel name = styledLabel(ki, FONT_BODY, has ? TEXT : TEXT_DIM);
            row.add(name);
            row.setAlignmentX(Component.LEFT_ALIGNMENT);
            content.add(row);
        }

        content.add(Box.createVerticalStrut(16));
        JButton closeBtn = createSmallButton("Close");
        closeBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        closeBtn.addActionListener(e -> dialog.dispose());
        content.add(closeBtn);

        dialog.setContentPane(content);
        dialog.setVisible(true);
    }

    private void showHelpDialog() {
        JDialog dialog = new JDialog(this, "Help", true);
        dialog.setSize(440, 340);
        dialog.setLocationRelativeTo(this);
        dialog.setUndecorated(true);
        dialog.getRootPane().setBorder(new LineBorder(BORDER_COLOR, 1, true));

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(BG_PANEL);
        content.setBorder(new EmptyBorder(16, 20, 16, 20));

        JLabel title = styledLabel("\u2753 COMMANDS AND HELP", FONT_SUBTITLE, TEXT);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(title);
        content.add(Box.createVerticalStrut(12));

        String[][] help = {
                { "Move", "Click exits on the map or use buttons" },
                { "Fight", "Click 'Fight' on the enemy card" },
                { "INVENTORY", "Right panel: Use, Equip or Drop items" },
                { "OBJECTIVES", "Press the 'OBJECTIVES' button at the top" },
                { "Ending", "Go to Mix Market and press 'CHOOSE ENDING'" },
                { "Minimap", "The map on the left shows your position" },
        };

        for (String[] h : help) {
            JPanel row = new JPanel(new BorderLayout());
            row.setOpaque(false);
            row.setBorder(new EmptyBorder(3, 0, 3, 0));
            JLabel key = styledLabel(h[0], new Font("Segoe UI", Font.BOLD, 12), ACCENT_CYAN);
            key.setPreferredSize(new Dimension(80, 20));
            row.add(key, BorderLayout.WEST);
            JLabel val = styledLabel(h[1], FONT_SMALL, TEXT_DIM);
            row.add(val, BorderLayout.CENTER);
            row.setAlignmentX(Component.LEFT_ALIGNMENT);
            content.add(row);
        }

        content.add(Box.createVerticalStrut(16));
        JButton closeBtn = createSmallButton("Close");
        closeBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        closeBtn.addActionListener(e -> dialog.dispose());
        content.add(closeBtn);

        dialog.setContentPane(content);
        dialog.setVisible(true);
    }

    

    private void updateTopBar() {
        if (protagonist == null)
            return;
        int pct = (int) ((double) protagonist.getHealth() / protagonist.getMaxHealth() * 100);
        topHPBar.setValue(pct);
        topHPBar.setForeground(hpColor(pct));
        topHPVal.setText(protagonist.getHealth() + "/" + protagonist.getMaxHealth());
        topXPVal.setText("XP " + protagonist.getExperience());
        topLVLVal.setText("Lv." + protagonist.getLevel());
    }

    private void updateStatsPanel() {
        if (protagonist == null)
            return;
        statHP.setText(protagonist.getHealth() + "/" + protagonist.getMaxHealth());
        statSTR.setText(protagonist.getStrength() + "/" + protagonist.getMaxStrength());
        statATK.setText(protagonist.getAttack() + "/" + protagonist.getMaxAttack());
        statDEF.setText(protagonist.getDefense() + "/" + protagonist.getMaxDefense());
        statXP.setText(String.valueOf(protagonist.getExperience()));
        statLVL.setText(String.valueOf(protagonist.getLevel()));

        int pct = (int) ((double) protagonist.getHealth() / protagonist.getMaxHealth() * 100);
        hpBarStat.setValue(pct);
        hpBarStat.setForeground(hpColor(pct));

        Shield Shield = protagonist.getEquippedShield();
        if (Shield != null) {
            shieldPanel.setVisible(true);
            shieldLabel.setText(Shield.getName() + " (Dif: " + Shield.getDefense() + ")");
        } else {
            shieldPanel.setVisible(false);
        }
    }

    private void updateInventoryPanel() {
        if (protagonist == null)
            return;
        inventoryListPanel.removeAll();
        weightLabel.setText(protagonist.getInventoryWeight() + "/" + protagonist.getMaxInventoryWeight());

        if (protagonist.getInventory().isEmpty()) {
            JLabel empty = styledLabel("INVENTORY empty", FONT_SMALL, TEXT_MUTED);
            empty.setBorder(new EmptyBorder(16, 8, 16, 8));
            inventoryListPanel.add(empty);
        } else {
            for (int i = 0; i < protagonist.getInventory().size(); i++) {
                inventoryListPanel.add(createInventoryItem(protagonist.getInventory().get(i), i));
                inventoryListPanel.add(Box.createVerticalStrut(2));
            }
        }
        inventoryListPanel.revalidate();
        inventoryListPanel.repaint();
    }

    

    private JPanel createEnemyCard(GameCharacter enemy) {
        JPanel card = new JPanel(new BorderLayout(10, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(BORDER_COLOR);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                
                g2.setColor(HP_RED);
                g2.fillRoundRect(0, 0, 3, getHeight(), 3, 3);
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(10, 14, 10, 10));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        
        JLabel avatar = styledLabel("[!]", new Font("Segoe UI", Font.BOLD, 18), HP_RED);
        card.add(avatar, BorderLayout.WEST);

        
        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setOpaque(false);
        JLabel name = styledLabel(enemy.getName(), new Font("Segoe UI", Font.BOLD, 13), TEXT);
        info.add(name);
        JLabel desc = styledLabel(enemy.getDescription(), new Font("Segoe UI", Font.PLAIN, 10), TEXT_MUTED);
        info.add(desc);

        
        int pct = (int) ((double) enemy.getHealth() / enemy.getMaxHealth() * 100);
        JProgressBar hpBar = createStyledProgressBar(pct, HP_RED);
        hpBar.setPreferredSize(new Dimension(0, 4));
        hpBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 4));
        info.add(Box.createVerticalStrut(3));
        info.add(hpBar);

        JLabel hpText = styledLabel(enemy.getHealth() + "/" + enemy.getMaxHealth(), FONT_MONO_SM, HP_RED);
        info.add(hpText);

        card.add(info, BorderLayout.CENTER);

        
        JButton fightBtn = new JButton("Fight") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2.setColor(new Color(220, 38, 38));
                } else {
                    g2.setColor(HP_RED);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
                        (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
            }
        };
        fightBtn.setOpaque(false);
        fightBtn.setContentAreaFilled(false);
        fightBtn.setBorderPainted(false);
        fightBtn.setFocusPainted(false);
        fightBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        fightBtn.setPreferredSize(new Dimension(80, 30));
        fightBtn.addActionListener(e -> startCombat(enemy));
        card.add(fightBtn, BorderLayout.EAST);

        return card;
    }

    private JPanel createItemCard(Item item, int index) {
        JPanel card = new JPanel(new BorderLayout(8, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(BORDER_COLOR);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(8, 12, 8, 10));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));

        String icon = "*";
        if (item instanceof Weapon)
            icon = ">";
        else if (item instanceof Shield)
            icon = "O";
        else if (item instanceof Heal)
            icon = "+";
        else if (item instanceof PowerPotion)
            icon = "^";
        else if (item instanceof PoisonPotion)
            icon = "~";
        JLabel ic = styledLabel(icon, new Font("Segoe UI", Font.BOLD, 18), ACCENT_YELLOW);
        card.add(ic, BorderLayout.WEST);

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setOpaque(false);
        JLabel name = styledLabel(item.getName(), new Font("Segoe UI", Font.BOLD, 12), TEXT);
        info.add(name);
        JLabel desc = styledLabel("<html>" + item.getDescription() + "</html>", new Font("Segoe UI", Font.PLAIN, 10),
                TEXT_MUTED);
        info.add(desc);
        card.add(info, BorderLayout.CENTER);

        JButton takeBtn = createSmallColorButton("Take", ACCENT_YELLOW);
        takeBtn.addActionListener(e -> pickUpItem(index));
        card.add(takeBtn, BorderLayout.EAST);

        return card;
    }

    private JPanel createInventoryItem(Transportable item, int index) {
        
        boolean hasUsura = (item instanceof Weapon) || (item instanceof Shield);
        int cardHeight = hasUsura ? 62 : 48;

        JPanel card = new JPanel(new BorderLayout(6, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(BORDER_COLOR);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(6, 8, 6, 6));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, cardHeight));

        String icon = "*";
        String type = "ITEM";
        if (item instanceof Weapon) {
            icon = ">";
            type = "Weapon";
        } else if (item instanceof Shield) {
            icon = "O";
            type = "Shield";
        } else if (item instanceof Heal) {
            icon = "+";
            type = "Heal";
        } else if (item instanceof PowerPotion) {
            icon = "^";
            type = "PowerPotion";
        } else if (item instanceof PoisonPotion) {
            icon = "~";
            type = "PoisonPotion";
        }

        JLabel ic = styledLabel(icon, FONT_BODY, TEXT);
        card.add(ic, BorderLayout.WEST);

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setOpaque(false);
        JLabel name = styledLabel(item.getName(), new Font("Segoe UI", Font.PLAIN, 11), TEXT);
        info.add(name);
        JLabel typeLabel = styledLabel(type + " | Weight: " + item.getWeight(), new Font("Segoe UI", Font.PLAIN, 9),
                TEXT_MUTED);
        info.add(typeLabel);

        
        if (hasUsura) {
            int usura = 0;
            int maxUsura = 100;
            boolean broken = false;
            if (item instanceof Weapon) {
                Weapon a = (Weapon) item;
                usura = a.getDurability();
                broken = !a.getWeaponState();
            } else if (item instanceof Shield) {
                Shield s = (Shield) item;
                usura = s.getDurability();
                broken = !s.getShieldState();
            }
            int pct = (usura * 100) / Math.max(1, maxUsura);
            Color barColor;
            if (broken || pct <= 0)
                barColor = new Color(120, 30, 30);
            else if (pct <= 30)
                barColor = HP_RED;
            else if (pct <= 60)
                barColor = ACCENT_YELLOW;
            else
                barColor = ACCENT_GREEN;

            JProgressBar usuraBar = new JProgressBar(0, 100);
            usuraBar.setValue(pct);
            usuraBar.setForeground(barColor);
            usuraBar.setBackground(new Color(30, 30, 40));
            usuraBar.setBorderPainted(false);
            usuraBar.setPreferredSize(new Dimension(0, 4));
            usuraBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 4));
            String status = broken ? "ROTTO" : (pct + "% integro");
            usuraBar.setToolTipText("Usura: " + status);
            info.add(Box.createVerticalStrut(2));
            info.add(usuraBar);
        }

        card.add(info, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 2, 0));
        actions.setOpaque(false);

        if (item instanceof Potion || item instanceof Item) {
            JButton useBtn = createTinyButton("Use", ACCENT_GREEN);
            useBtn.addActionListener(e -> useItem(index));
            actions.add(useBtn);
        }
        if (item instanceof Shield) {
            JButton eqBtn = createTinyButton("Equip", ACCENT_BLUE);
            eqBtn.addActionListener(e -> equipItem(index));
            actions.add(eqBtn);
        }
        JButton dropBtn = createTinyButton("X", TEXT_MUTED);
        dropBtn.addActionListener(e -> dropItem(index));
        actions.add(dropBtn);

        card.add(actions, BorderLayout.EAST);
        return card;
    }

    private JButton createExitButton(String command, String destination) {
        JButton btn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = getModel().isRollover() ? BG_CARD_HOVER : BG_CARD;
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                Color brd = getModel().isRollover() ? new Color(78, 205, 196, 80) : BORDER_COLOR;
                g2.setColor(brd);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);

                g2.setColor(ACCENT_CYAN);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
                g2.drawString("\u2192", 10, 20);

                g2.setColor(TEXT);
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                g2.drawString(command, 28, 18);

                g2.setColor(TEXT_MUTED);
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                g2.drawString(destination, 28, 32);
            }
        };
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(0, 42));
        return btn;
    }

    

    private JLabel styledLabel(String text, Font font, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(font);
        l.setForeground(color);
        return l;
    }

    private JPanel createSectionHeader(String text) {
        JPanel h = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        h.setBackground(BG_CARD);
        h.setBorder(new EmptyBorder(8, 14, 8, 14));
        JLabel l = styledLabel(text, FONT_HEADER, TEXT_DIM);
        h.add(l);
        return h;
    }

    private JPanel createCollapsibleSection(String title) {
        JPanel sect = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_PANEL);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(BORDER_COLOR);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
            }
        };
        sect.setOpaque(false);
        sect.setMaximumSize(new Dimension(Integer.MAX_VALUE, 500));

        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.fillRect(0, getHeight() - 6, getWidth(), 6);
            }
        };
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(8, 12, 8, 12));
        header.add(styledLabel(title, FONT_HEADER, TEXT_DIM));
        sect.add(header, BorderLayout.NORTH);

        return sect;
    }

    private JButton createAccentButton(String text, int fontSize) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, ACCENT, getWidth(), getHeight(), new Color(192, 57, 43));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                if (getModel().isRollover()) {
                    g2.setColor(new Color(255, 255, 255, 25));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                }
                
                super.paintComponent(g);
            }
        };
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, fontSize));
        btn.setPreferredSize(new Dimension(380, 52));
        btn.setMaximumSize(new Dimension(380, 52));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton createCombatButton(String text, Color accent) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = isEnabled()
                        ? (getModel().isRollover() ? new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 40)
                                : BG_PANEL)
                        : BG_CARD;
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                Color brd = isEnabled() ? (getModel().isRollover() ? accent : BORDER_COLOR) : new Color(40, 40, 50);
                g2.setColor(brd);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                g2.setColor(isEnabled() ? Color.WHITE : TEXT_MUTED);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
                        (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
            }
        };
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(0, 50));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton createSmallButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? BG_CARD_HOVER : BG_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(BORDER_COLOR);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                g2.setColor(TEXT_DIM);
                g2.setFont(FONT_SMALL);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
                        (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
            }
        };
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(90, 30));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton createSmallColorButton(String text, Color color) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(),
                        getModel().isRollover() ? 50 : 25));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 80));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                g2.setColor(color);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
                        (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
            }
        };
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(70, 26));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton createTinyButton(String text, Color color) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? new Color(color.getRed(), color.getGreen(), color.getBlue(), 35)
                        : new Color(0, 0, 0, 0));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                g2.setColor(getModel().isRollover() ? color : BORDER_COLOR);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 6, 6);
                g2.setColor(getModel().isRollover() ? color : TEXT_MUTED);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 9));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
                        (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
            }
        };
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(38, 20));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton createOptionButton(String text, String detail, Color accent) {
        JButton btn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(
                        getModel().isRollover() ? new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 25)
                                : BG_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(
                        getModel().isRollover() ? new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 80)
                                : BORDER_COLOR);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.setColor(TEXT);
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                g2.drawString(text, 12, 18);
                if (!detail.isEmpty()) {
                    g2.setColor(accent);
                    g2.setFont(FONT_MONO_SM);
                    FontMetrics fm = g2.getFontMetrics();
                    g2.drawString(detail, getWidth() - fm.stringWidth(detail) - 12, 18);
                }
            }
        };
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(0, 30));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton createEndingButton(String title, String desc, Color accent) {
        JButton btn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? BG_CARD_HOVER : BG_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(getModel().isRollover() ? accent : BORDER_COLOR);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                g2.setColor(TEXT);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
                g2.drawString(title, 16, 24);
                g2.setColor(TEXT_MUTED);
                g2.setFont(FONT_SMALL);
                g2.drawString(desc, 16, 42);
            }
        };
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));
        btn.setPreferredSize(new Dimension(0, 56));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JProgressBar createStyledProgressBar(int value, Color fg) {
        JProgressBar bar = new JProgressBar(0, 100);
        bar.setValue(value);
        bar.setForeground(fg);
        bar.setBackground(BG_CARD);
        bar.setBorderPainted(false);
        bar.setStringPainted(false);
        bar.setUI(new javax.swing.plaf.basic.BasicProgressBarUI() {
            @Override
            protected void paintDeterminate(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = c.getWidth(), h = c.getHeight();
                g2.setColor(c.getBackground());
                g2.fillRoundRect(0, 0, w, h, h, h);
                int fillW = (int) (w * ((double) bar.getValue() / bar.getMaximum()));
                if (fillW > 0) {
                    g2.setColor(c.getForeground());
                    g2.fillRoundRect(0, 0, fillW, h, h, h);
                }
            }
        });
        return bar;
    }

    private Color hpColor(int pct) {
        if (pct > 60)
            return ACCENT_GREEN;
        if (pct > 30)
            return ACCENT_YELLOW;
        return HP_RED;
    }

    private void styleScrollBar(JScrollPane sp) {
        sp.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.trackColor = BG_PANEL;
                this.thumbColor = TEXT_MUTED;
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton btn = new JButton();
                btn.setPreferredSize(new Dimension(0, 0));
                return btn;
            }

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(100, 100, 130, 80));
                g2.fillRoundRect(r.x + 1, r.y, r.width - 2, r.height, 8, 8);
            }

            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
                g.setColor(BG_PANEL);
                g.fillRect(r.x, r.y, r.width, r.height);
            }
        });
        sp.getVerticalScrollBar().setPreferredSize(new Dimension(6, 0));
    }

    private void addLog(String msg) {
        if (gameLogArea == null)
            return;
        String time = String.format("[%tT] ", System.currentTimeMillis());
        gameLogArea.append(time + msg + "\n");
        gameLogArea.setCaretPosition(gameLogArea.getDocument().getLength());
    }

    private void addCombatLog(String msg) {
        if (combatLogArea == null)
            return;
        combatLogArea.append(msg + "\n");
        combatLogArea.setCaretPosition(combatLogArea.getDocument().getLength());
    }

    

    class MinimapPanel extends JPanel {
        private float pulsePhase = 0;
        private final javax.swing.Timer pulseTimer;

        MinimapPanel() {
            setOpaque(false);
            setPreferredSize(new Dimension(236, 300));
            pulseTimer = new javax.swing.Timer(50, e -> {
                pulsePhase += 0.08f;
                if (pulsePhase > 2 * Math.PI)
                    pulsePhase -= (float) (2 * Math.PI);
                repaint();
            });
            pulseTimer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

            int w = getWidth(), h = getHeight();
            int pad = 18;
            int mapW = w - pad * 2, mapH = h - pad * 2;

            
            g2.setColor(BG_CARD);
            g2.fillRoundRect(0, 0, w, h, 14, 14);
            g2.setColor(BORDER_COLOR);
            g2.drawRoundRect(0, 0, w - 1, h - 1, 14, 14);

            if (World == null)
                return;

            Location current = World.getCurrentPosition();
            String currentName = current != null ? current.getName() : "";

            
            g2.setStroke(new BasicStroke(1.5f));
            Set<String> drawnEdges = new HashSet<>();
            for (Location Location : World.getLocations()) {
                double[] pos1 = MINIMAP_POS.get(Location.getName());
                if (pos1 == null)
                    continue;
                int x1 = pad + (int) (pos1[0] * mapW);
                int y1 = pad + (int) (pos1[1] * mapH);

                for (Map.Entry<String, Location> conn : Location.getConnections().entrySet()) {
                    double[] pos2 = MINIMAP_POS.get(conn.getValue().getName());
                    if (pos2 == null)
                        continue;
                    String edgeKey = Location.getName().compareTo(conn.getValue().getName()) < 0
                            ? Location.getName() + "|" + conn.getValue().getName()
                            : conn.getValue().getName() + "|" + Location.getName();
                    if (drawnEdges.add(edgeKey)) {
                        int x2 = pad + (int) (pos2[0] * mapW);
                        int y2 = pad + (int) (pos2[1] * mapH);

                        boolean involves_current = Location.getName().equals(currentName)
                                || conn.getValue().getName().equals(currentName);
                        if (involves_current) {
                            g2.setColor(new Color(232, 69, 69, 50));
                            g2.setStroke(new BasicStroke(2f));
                        } else {
                            g2.setColor(new Color(255, 255, 255, 15));
                            g2.setStroke(new BasicStroke(1f));
                        }
                        g2.drawLine(x1, y1, x2, y2);
                    }
                }
            }

            
            for (Location Location : World.getLocations()) {
                double[] pos = MINIMAP_POS.get(Location.getName());
                if (pos == null)
                    continue;
                int x = pad + (int) (pos[0] * mapW);
                int y = pad + (int) (pos[1] * mapH);
                boolean isCurrent = Location.getName().equals(currentName);
                boolean visited = visitedLocations.contains(Location.getName());
                boolean hasEnemies = !Location
                        .getEnemies(protagonist != null ? protagonist : new GameCharacter("", "", -1, -1, -1, -1))
                        .isEmpty();
                boolean hasItems = !Location.getItems().isEmpty();

                int r = isCurrent ? 8 : 5;

                if (isCurrent) {
                    
                    float pulse = (float) (0.3 + 0.3 * Math.sin(pulsePhase));
                    g2.setColor(new Color(232, 69, 69, (int) (pulse * 100)));
                    g2.fillOval(x - 14, y - 14, 28, 28);
                    g2.setColor(ACCENT);
                    g2.fillOval(x - r, y - r, r * 2, r * 2);
                    
                    g2.setColor(new Color(255, 255, 255, 200));
                    g2.fillOval(x - 2, y - 2, 4, 4);
                } else if (hasEnemies) {
                    g2.setColor(HP_RED);
                    g2.fillOval(x - r, y - r, r * 2, r * 2);
                    
                    g2.setColor(new Color(239, 68, 68, 40));
                    g2.drawOval(x - r - 3, y - r - 3, (r + 3) * 2, (r + 3) * 2);
                } else if (hasItems) {
                    g2.setColor(ACCENT_YELLOW);
                    g2.fillOval(x - r, y - r, r * 2, r * 2);
                } else if (visited) {
                    g2.setColor(ACCENT_CYAN);
                    g2.fillOval(x - r, y - r, r * 2, r * 2);
                } else {
                    g2.setColor(TEXT_MUTED);
                    g2.fillOval(x - r, y - r, r * 2, r * 2);
                }

                
                g2.setFont(new Font("Segoe UI", isCurrent ? Font.BOLD : Font.PLAIN, isCurrent ? 9 : 8));
                g2.setColor(isCurrent ? TEXT : TEXT_DIM);
                String shortName = shortenName(Location.getName());
                FontMetrics fm = g2.getFontMetrics();
                int tw = fm.stringWidth(shortName);
                int tx = x - tw / 2;
                int ty = y + r + 12;
                
                if (tx < 4)
                    tx = 4;
                if (tx + tw > w - 4)
                    tx = w - 4 - tw;
                if (ty > h - 4)
                    ty = y - r - 4;
                g2.drawString(shortName, tx, ty);
            }
        }

        private String shortenName(String name) {
            if (name.length() <= 14)
                return name;
            
            switch (name) {
                case "stazione":
                    return "station";
                case "Delicious Kebab":
                    return "kebab";
                case "Roma Street - Brawl":
                    return "brawl";
                case "Tobacco Shop Back":
                    return "Tabacchino";
                case "Underground Tunnels":
                    return "Tunnels";
                case "Underground Parking":
                    return "parking";
                case "Abandoned Building":
                    return "building";
                case "Building Rooftop":
                    return "roof";
                case "Central Zone":
                    return "Zona Caos";
                case "End of the Street":
                    return "Fondo Via";
                case "Mix Market":
                    return "Mix Market";
                default:
                    return name.substring(0, 12) + "..";
            }
        }
    }

    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }

            
            UIManager.put("Panel.background", BG_DARK);
            UIManager.put("OptionPane.background", BG_PANEL);
            UIManager.put("OptionPane.messageForeground", TEXT);

            GameGUI gui = new GameGUI();
            gui.setVisible(true);
        });
    }
}
