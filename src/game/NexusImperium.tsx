"use client";

import React, { useEffect, useRef, useState } from 'react';
import { QuantumEngine } from '../engine/QuantumEngine';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';
import { Progress } from '@/components/ui/progress';
import { Slider } from '@/components/ui/slider';
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from '@/components/ui/dialog';
import { Alert, AlertDescription } from '@/components/ui/alert';
import {
  Rocket,
  Settings,
  Shield,
  Zap,
  Coins,
  Diamond,
  Globe,
  Sword,
  Building,
  FlaskConical,
  Ship,
  Crown,
  Users,
  Target,
  TrendingUp,
  Activity,
  Database,
  Power,
  Sparkles,
  Map,
  Eye,
  Radio,
  Timer,
  Star,
  Flame,
  Volume2,
  VolumeX,
  Play,
  Pause,
  RotateCcw,
  Menu,
  X,
  ArrowUp,
  ArrowDown,
  ArrowLeft,
  ArrowRight,
  Plus,
  Minus,
  Search,
  Filter,
  Maximize,
  Minimize,
  Info,
  Warning,
  Check,
  AlertTriangle,
  ChevronDown,
  ChevronUp
} from 'lucide-react';

// Game Configuration
interface GameConfig {
  galaxySize: 'small' | 'medium' | 'large' | 'enormous';
  galaxyAge: 'young' | 'mature' | 'ancient';
  playerCount: number;
  gameMode: 'conquest' | 'diplomatic' | 'research' | 'survival';
  difficulty: 'casual' | 'normal' | 'hardcore' | 'nightmare';
  realTimeEvents: boolean;
  quantumPhysics: boolean;
}

// Game Data Interfaces
interface Resource {
  name: string;
  amount: number;
  production: number;
  capacity: number;
  efficiency: number;
}

interface StarSystem {
  id: string;
  name: string;
  coordinates: string;
  owner?: string;
  planets: Planet[];
  strategic: boolean;
  threat: number;
  resources: Resource[];
}

interface Planet {
  id: string;
  name: string;
  type: string;
  population: number;
  infrastructure: number;
  defense: number;
  resources: Resource[];
  buildings: Building[];
}

interface Building {
  id: string;
  name: string;
  level: number;
  maxLevel: number;
  production: number;
  energyCost: number;
  upgrading: boolean;
  upgradeTime?: number;
}

interface Fleet {
  id: string;
  name: string;
  ships: Ship[];
  position: string;
  mission: string;
  destination?: string;
  eta?: string;
  strength: number;
  morale: number;
  supply: number;
}

interface Ship {
  id: string;
  name: string;
  class: string;
  health: number;
  maxHealth: number;
  attack: number;
  defense: number;
  speed: number;
  experience: number;
  modules: ShipModule[];
}

interface ShipModule {
  id: string;
  name: string;
  type: string;
  stats: Record<string, number>;
  energy: number;
}

interface Technology {
  id: string;
  name: string;
  level: number;
  progress: number;
  cost: number;
  benefits: string[];
  prerequisites: string[];
}

interface Faction {
  id: string;
  name: string;
  color: string;
  specialties: string[];
  bonuses: Record<string, number>;
  philosophy: string;
}

interface Player {
  id: string;
  name: string;
  faction: Faction;
  resources: Record<string, Resource>;
  territories: StarSystem[];
  fleets: Fleet[];
  technologies: Technology[];
  reputation: number;
  influence: number;
  score: number;
}

interface GameState {
  currentTurn: number;
  phase: 'planning' | 'action' | 'resolution' | 'event';
  players: Player[];
  galaxy: StarSystem[];
  events: GameEvent[];
  timeRemaining: number;
  isPaused: boolean;
}

interface GameEvent {
  id: string;
  type: 'political' | 'economic' | 'military' | 'natural' | 'anomaly';
  title: string;
  description: string;
  effects: Record<string, number>;
  duration: number;
  severity: 'low' | 'medium' | 'high' | 'critical';
}

// Main Game Component
export default function NexusImperium() {
  const canvasRef = useRef<HTMLCanvasElement>(null);
  const engineRef = useRef<QuantumEngine | null>(null);
  const [gameState, setGameState] = useState<GameState | null>(null);
  const [selectedPlayer, setSelectedPlayer] = useState<string>('');
  const [activeTab, setActiveTab] = useState('overview');
  const [gameConfig, setGameConfig] = useState<GameConfig>({
    galaxySize: 'medium',
    galaxyAge: 'mature',
    playerCount: 4,
    gameMode: 'conquest',
    difficulty: 'normal',
    realTimeEvents: true,
    quantumPhysics: true
  });
  const [isGameStarted, setIsGameStarted] = useState(false);
  const [soundEnabled, setSoundEnabled] = useState(true);
  const [currentTime, setCurrentTime] = useState(new Date());
  const [fps, setFps] = useState(0);
  const [isFullscreen, setIsFullscreen] = useState(false);
  const [showSettings, setShowSettings] = useState(false);
  const [selectedSystem, setSelectedSystem] = useState<StarSystem | null>(null);
  const [selectedFleet, setSelectedFleet] = useState<Fleet | null>(null);
  const [zoomLevel, setZoomLevel] = useState(1);
  const [cameraPosition, setCameraPosition] = useState({ x: 0, y: 0 });
  const [notifications, setNotifications] = useState<GameEvent[]>([]);
  const [showStats, setShowStats] = useState(false);

  // Initialize game engine
  useEffect(() => {
    if (canvasRef.current && !engineRef.current) {
      try {
        engineRef.current = new QuantumEngine(canvasRef.current);
        engineRef.current.start();
        
        // Set up FPS monitoring
        const fpsInterval = setInterval(() => {
          if (engineRef.current) {
            setFps(engineRef.current.getFPS());
          }
        }, 1000);

        return () => {
          clearInterval(fpsInterval);
          if (engineRef.current) {
            engineRef.current.stop();
          }
        };
      } catch (error) {
        console.error('Failed to initialize game engine:', error);
      }
    }
  }, []);

  // Time update
  useEffect(() => {
    const timer = setInterval(() => {
      setCurrentTime(new Date());
    }, 1000);
    return () => clearInterval(timer);
  }, []);

  // Initialize game state
  useEffect(() => {
    if (isGameStarted && engineRef.current) {
      initializeGame();
    }
  }, [isGameStarted]);

  const initializeGame = () => {
    // Generate initial game state
    const mockPlayers: Player[] = [
      {
        id: 'player1',
        name: 'Commander Alpha',
        faction: {
          id: 'terran',
          name: 'Terran Federation',
          color: '#3b82f6',
          specialties: ['military', 'engineering'],
          bonuses: { shipProduction: 25, defenseBonus: 15 },
          philosophy: 'Unity through strength'
        },
        resources: {
          energy: { name: 'Energy', amount: 5000, production: 120, capacity: 10000, efficiency: 85 },
          matter: { name: 'Matter', amount: 3200, production: 80, capacity: 8000, efficiency: 90 },
          rare: { name: 'Rare Materials', amount: 850, production: 15, capacity: 2000, efficiency: 75 },
          exotic: { name: 'Exotic Matter', amount: 120, production: 2, capacity: 500, efficiency: 95 }
        },
        territories: [],
        fleets: [],
        technologies: [],
        reputation: 100,
        influence: 50,
        score: 0
      }
    ];

    const mockGalaxy: StarSystem[] = [
      {
        id: 'sol',
        name: 'Sol System',
        coordinates: '[0:0:0]',
        owner: 'player1',
        planets: [
          {
            id: 'earth',
            name: 'Earth',
            type: 'Terran',
            population: 8000000,
            infrastructure: 85,
            defense: 70,
            resources: [],
            buildings: []
          }
        ],
        strategic: true,
        threat: 0,
        resources: []
      }
    ];

    const initialState: GameState = {
      currentTurn: 1,
      phase: 'planning',
      players: mockPlayers,
      galaxy: mockGalaxy,
      events: [],
      timeRemaining: 300,
      isPaused: false
    };

    setGameState(initialState);
    setSelectedPlayer('player1');
  };

  const startGame = () => {
    setIsGameStarted(true);
  };

  const pauseGame = () => {
    if (gameState) {
      setGameState({ ...gameState, isPaused: !gameState.isPaused });
    }
  };

  const resetGame = () => {
    setIsGameStarted(false);
    setGameState(null);
    setSelectedPlayer('');
    setActiveTab('overview');
  };

  const toggleFullscreen = () => {
    if (!document.fullscreenElement) {
      document.documentElement.requestFullscreen();
      setIsFullscreen(true);
    } else {
      document.exitFullscreen();
      setIsFullscreen(false);
    }
  };

  const formatNumber = (num: number) => {
    if (num >= 1000000) {
      return (num / 1000000).toFixed(1) + 'M';
    } else if (num >= 1000) {
      return (num / 1000).toFixed(1) + 'K';
    }
    return num.toString();
  };

  const currentPlayer = gameState?.players.find(p => p.id === selectedPlayer);

  if (!isGameStarted) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-slate-900 via-blue-900 to-purple-900 flex items-center justify-center p-4">
        <div className="max-w-4xl w-full">
          <div className="text-center mb-8">
            <h1 className="text-6xl font-bold bg-gradient-to-r from-cyan-400 to-blue-400 bg-clip-text text-transparent mb-4">
              NEXUS IMPERIUM
            </h1>
            <p className="text-xl text-slate-300 mb-2">
              Advanced Galactic Strategy • Zero Pay-to-Win
            </p>
            <p className="text-sm text-slate-400">
              Powered by QuantumEngine3D • Revolutionary Gameplay
            </p>
          </div>

          <Card className="bg-slate-800/90 border-cyan-500/30 backdrop-blur-lg">
            <CardHeader>
              <CardTitle className="text-cyan-400 flex items-center gap-2">
                <Settings className="h-5 w-5" />
                Game Configuration
              </CardTitle>
            </CardHeader>
            <CardContent className="space-y-6">
              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div>
                  <label className="block text-sm font-medium text-slate-300 mb-2">
                    Galaxy Size
                  </label>
                  <select
                    value={gameConfig.galaxySize}
                    onChange={(e) => setGameConfig({...gameConfig, galaxySize: e.target.value as any})}
                    className="w-full p-3 bg-slate-700 border border-slate-600 rounded-lg text-white focus:ring-2 focus:ring-cyan-500"
                  >
                    <option value="small">Small (100 systems)</option>
                    <option value="medium">Medium (500 systems)</option>
                    <option value="large">Large (1000 systems)</option>
                    <option value="enormous">Enormous (2500 systems)</option>
                  </select>
                </div>

                <div>
                  <label className="block text-sm font-medium text-slate-300 mb-2">
                    Galaxy Age
                  </label>
                  <select
                    value={gameConfig.galaxyAge}
                    onChange={(e) => setGameConfig({...gameConfig, galaxyAge: e.target.value as any})}
                    className="w-full p-3 bg-slate-700 border border-slate-600 rounded-lg text-white focus:ring-2 focus:ring-cyan-500"
                  >
                    <option value="young">Young (Active, chaotic)</option>
                    <option value="mature">Mature (Balanced)</option>
                    <option value="ancient">Ancient (Mysterious ruins)</option>
                  </select>
                </div>

                <div>
                  <label className="block text-sm font-medium text-slate-300 mb-2">
                    Game Mode
                  </label>
                  <select
                    value={gameConfig.gameMode}
                    onChange={(e) => setGameConfig({...gameConfig, gameMode: e.target.value as any})}
                    className="w-full p-3 bg-slate-700 border border-slate-600 rounded-lg text-white focus:ring-2 focus:ring-cyan-500"
                  >
                    <option value="conquest">Conquest (Domination)</option>
                    <option value="diplomatic">Diplomatic (Alliances)</option>
                    <option value="research">Research (Technology)</option>
                    <option value="survival">Survival (Galactic Crisis)</option>
                  </select>
                </div>

                <div>
                  <label className="block text-sm font-medium text-slate-300 mb-2">
                    Difficulty
                  </label>
                  <select
                    value={gameConfig.difficulty}
                    onChange={(e) => setGameConfig({...gameConfig, difficulty: e.target.value as any})}
                    className="w-full p-3 bg-slate-700 border border-slate-600 rounded-lg text-white focus:ring-2 focus:ring-cyan-500"
                  >
                    <option value="casual">Casual (Relaxed)</option>
                    <option value="normal">Normal (Balanced)</option>
                    <option value="hardcore">Hardcore (Challenging)</option>
                    <option value="nightmare">Nightmare (Expert)</option>
                  </select>
                </div>
              </div>

              <div className="space-y-4">
                <div>
                  <label className="block text-sm font-medium text-slate-300 mb-2">
                    Player Count: {gameConfig.playerCount}
                  </label>
                  <Slider
                    value={[gameConfig.playerCount]}
                    onValueChange={(value) => setGameConfig({...gameConfig, playerCount: value[0]})}
                    max={16}
                    min={2}
                    step={1}
                    className="w-full"
                  />
                </div>

                <div className="flex items-center gap-6">
                  <label className="flex items-center gap-2 text-sm text-slate-300">
                    <input
                      type="checkbox"
                      checked={gameConfig.realTimeEvents}
                      onChange={(e) => setGameConfig({...gameConfig, realTimeEvents: e.target.checked})}
                      className="w-4 h-4 text-cyan-600 bg-slate-700 border-slate-600 rounded focus:ring-cyan-500"
                    />
                    Real-time Events
                  </label>
                  <label className="flex items-center gap-2 text-sm text-slate-300">
                    <input
                      type="checkbox"
                      checked={gameConfig.quantumPhysics}
                      onChange={(e) => setGameConfig({...gameConfig, quantumPhysics: e.target.checked})}
                      className="w-4 h-4 text-cyan-600 bg-slate-700 border-slate-600 rounded focus:ring-cyan-500"
                    />
                    Quantum Physics
                  </label>
                </div>
              </div>

              <div className="flex justify-center pt-6">
                <Button
                  onClick={startGame}
                  size="lg"
                  className="bg-gradient-to-r from-cyan-500 to-blue-500 hover:from-cyan-600 hover:to-blue-600 text-white px-8 py-3 text-lg font-semibold"
                >
                  <Play className="h-5 w-5 mr-2" />
                  Launch Game
                </Button>
              </div>
            </CardContent>
          </Card>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-black overflow-hidden relative">
      {/* Game Canvas */}
      <canvas
        ref={canvasRef}
        className="absolute inset-0 w-full h-full"
        style={{ background: 'radial-gradient(ellipse at center, #1a1a2e 0%, #000 100%)' }}
      />

      {/* Game UI Overlay */}
      <div className="absolute inset-0 pointer-events-none">
        <div className="pointer-events-auto">
          {/* Top Bar */}
          <div className="bg-slate-900/95 backdrop-blur-lg border-b border-cyan-500/30 p-4">
            <div className="flex items-center justify-between">
              {/* Game Title & Status */}
              <div className="flex items-center gap-4">
                <div className="flex items-center gap-2">
                  <Rocket className="h-6 w-6 text-cyan-400" />
                  <h1 className="text-xl font-bold text-cyan-400">NEXUS IMPERIUM</h1>
                </div>
                <div className="flex items-center gap-4 text-sm text-slate-400">
                  <span>Turn {gameState?.currentTurn}</span>
                  <span>{gameState?.phase.toUpperCase()}</span>
                  <span>{currentTime.toLocaleTimeString()}</span>
                  <span>FPS: {fps}</span>
                </div>
              </div>

              {/* Resources */}
              {currentPlayer && (
                <div className="flex items-center gap-6">
                  {Object.entries(currentPlayer.resources).map(([key, resource]) => (
                    <div key={key} className="flex items-center gap-2 px-3 py-1 bg-slate-800/50 rounded-lg">
                      <div className={`w-3 h-3 rounded-full ${
                        key === 'energy' ? 'bg-yellow-400' :
                        key === 'matter' ? 'bg-blue-400' :
                        key === 'rare' ? 'bg-purple-400' : 'bg-cyan-400'
                      }`} />
                      <span className="text-white font-mono text-sm">
                        {formatNumber(resource.amount)}
                      </span>
                      <span className="text-green-400 text-xs">
                        +{resource.production}
                      </span>
                    </div>
                  ))}
                </div>
              )}

              {/* Controls */}
              <div className="flex items-center gap-2">
                <Button
                  variant="ghost"
                  size="icon"
                  onClick={() => setSoundEnabled(!soundEnabled)}
                  className="text-cyan-400 hover:bg-cyan-500/20"
                >
                  {soundEnabled ? <Volume2 className="h-4 w-4" /> : <VolumeX className="h-4 w-4" />}
                </Button>
                <Button
                  variant="ghost"
                  size="icon"
                  onClick={pauseGame}
                  className="text-cyan-400 hover:bg-cyan-500/20"
                >
                  {gameState?.isPaused ? <Play className="h-4 w-4" /> : <Pause className="h-4 w-4" />}
                </Button>
                <Button
                  variant="ghost"
                  size="icon"
                  onClick={() => setShowSettings(!showSettings)}
                  className="text-cyan-400 hover:bg-cyan-500/20"
                >
                  <Settings className="h-4 w-4" />
                </Button>
                <Button
                  variant="ghost"
                  size="icon"
                  onClick={toggleFullscreen}
                  className="text-cyan-400 hover:bg-cyan-500/20"
                >
                  {isFullscreen ? <Minimize className="h-4 w-4" /> : <Maximize className="h-4 w-4" />}
                </Button>
              </div>
            </div>
          </div>

          {/* Main Game Interface */}
          <div className="flex h-screen pt-16">
            {/* Left Panel */}
            <div className="w-80 bg-slate-900/95 backdrop-blur-lg border-r border-cyan-500/30 flex flex-col">
              <Tabs value={activeTab} onValueChange={setActiveTab} className="flex-1">
                <TabsList className="grid w-full grid-cols-4 bg-slate-800/50 border-b border-cyan-500/30">
                  <TabsTrigger value="overview" className="data-[state=active]:bg-cyan-500/20">
                    <Globe className="h-4 w-4" />
                  </TabsTrigger>
                  <TabsTrigger value="fleets" className="data-[state=active]:bg-cyan-500/20">
                    <Ship className="h-4 w-4" />
                  </TabsTrigger>
                  <TabsTrigger value="research" className="data-[state=active]:bg-cyan-500/20">
                    <FlaskConical className="h-4 w-4" />
                  </TabsTrigger>
                  <TabsTrigger value="diplomacy" className="data-[state=active]:bg-cyan-500/20">
                    <Users className="h-4 w-4" />
                  </TabsTrigger>
                </TabsList>

                <TabsContent value="overview" className="flex-1 p-4 space-y-4">
                  <Card className="bg-slate-800/50 border-cyan-500/30">
                    <CardHeader>
                      <CardTitle className="text-cyan-400 text-lg">Empire Overview</CardTitle>
                    </CardHeader>
                    <CardContent className="space-y-4">
                      {currentPlayer && (
                        <div className="space-y-3">
                          <div className="flex items-center justify-between">
                            <span className="text-slate-300">Territories</span>
                            <span className="text-white font-mono">{currentPlayer.territories.length}</span>
                          </div>
                          <div className="flex items-center justify-between">
                            <span className="text-slate-300">Reputation</span>
                            <div className="flex items-center gap-2">
                              <Progress value={currentPlayer.reputation} className="w-20" />
                              <span className="text-white font-mono text-sm">{currentPlayer.reputation}%</span>
                            </div>
                          </div>
                          <div className="flex items-center justify-between">
                            <span className="text-slate-300">Influence</span>
                            <span className="text-white font-mono">{currentPlayer.influence}</span>
                          </div>
                          <div className="flex items-center justify-between">
                            <span className="text-slate-300">Score</span>
                            <span className="text-white font-mono">{formatNumber(currentPlayer.score)}</span>
                          </div>
                        </div>
                      )}
                    </CardContent>
                  </Card>

                  <Card className="bg-slate-800/50 border-cyan-500/30">
                    <CardHeader>
                      <CardTitle className="text-cyan-400 text-lg">Recent Events</CardTitle>
                    </CardHeader>
                    <CardContent>
                      <div className="space-y-2">
                        {notifications.length === 0 ? (
                          <p className="text-slate-400 text-sm">No recent events</p>
                        ) : (
                          notifications.slice(0, 3).map((event, index) => (
                            <div key={index} className="p-2 bg-slate-700/50 rounded border-l-2 border-cyan-500">
                              <p className="text-sm text-white">{event.title}</p>
                              <p className="text-xs text-slate-400">{event.description}</p>
                            </div>
                          ))
                        )}
                      </div>
                    </CardContent>
                  </Card>
                </TabsContent>

                <TabsContent value="fleets" className="flex-1 p-4">
                  <Card className="bg-slate-800/50 border-cyan-500/30">
                    <CardHeader>
                      <CardTitle className="text-cyan-400 text-lg">Fleet Management</CardTitle>
                    </CardHeader>
                    <CardContent>
                      <p className="text-slate-400 text-sm">Fleet interface coming soon...</p>
                    </CardContent>
                  </Card>
                </TabsContent>

                <TabsContent value="research" className="flex-1 p-4">
                  <Card className="bg-slate-800/50 border-cyan-500/30">
                    <CardHeader>
                      <CardTitle className="text-cyan-400 text-lg">Research & Development</CardTitle>
                    </CardHeader>
                    <CardContent>
                      <p className="text-slate-400 text-sm">Research interface coming soon...</p>
                    </CardContent>
                  </Card>
                </TabsContent>

                <TabsContent value="diplomacy" className="flex-1 p-4">
                  <Card className="bg-slate-800/50 border-cyan-500/30">
                    <CardHeader>
                      <CardTitle className="text-cyan-400 text-lg">Galactic Diplomacy</CardTitle>
                    </CardHeader>
                    <CardContent>
                      <p className="text-slate-400 text-sm">Diplomacy interface coming soon...</p>
                    </CardContent>
                  </Card>
                </TabsContent>
              </Tabs>
            </div>

            {/* Right Panel */}
            <div className="w-80 bg-slate-900/95 backdrop-blur-lg border-l border-cyan-500/30 flex flex-col">
              <div className="p-4 border-b border-cyan-500/30">
                <h2 className="text-cyan-400 font-semibold">Galaxy Map</h2>
              </div>
              <div className="flex-1 p-4">
                <Card className="bg-slate-800/50 border-cyan-500/30 h-full">
                  <CardContent className="p-4 h-full flex items-center justify-center">
                    <div className="text-center">
                      <Map className="h-12 w-12 text-cyan-400 mx-auto mb-2" />
                      <p className="text-slate-400 text-sm">3D Galaxy Map</p>
                      <p className="text-slate-500 text-xs">Powered by QuantumEngine</p>
                    </div>
                  </CardContent>
                </Card>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Settings Modal */}
      {showSettings && (
        <div className="absolute inset-0 bg-black/50 flex items-center justify-center z-50">
          <Card className="bg-slate-900 border-cyan-500/30 w-96">
            <CardHeader className="flex flex-row items-center justify-between">
              <CardTitle className="text-cyan-400">Game Settings</CardTitle>
              <Button
                variant="ghost"
                size="icon"
                onClick={() => setShowSettings(false)}
                className="text-cyan-400 hover:bg-cyan-500/20"
              >
                <X className="h-4 w-4" />
              </Button>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="space-y-2">
                <label className="text-sm font-medium text-slate-300">Graphics Quality</label>
                <select className="w-full p-2 bg-slate-700 border border-slate-600 rounded text-white">
                  <option>Ultra</option>
                  <option>High</option>
                  <option>Medium</option>
                  <option>Low</option>
                </select>
              </div>
              <div className="space-y-2">
                <label className="text-sm font-medium text-slate-300">Sound Volume</label>
                <Slider defaultValue={[70]} max={100} step={1} />
              </div>
              <div className="flex justify-end gap-2">
                <Button variant="outline" onClick={() => setShowSettings(false)}>
                  Cancel
                </Button>
                <Button onClick={() => setShowSettings(false)}>
                  Save
                </Button>
              </div>
            </CardContent>
          </Card>
        </div>
      )}

      {/* Performance Stats */}
      {showStats && (
        <div className="absolute top-20 left-4 bg-slate-900/95 backdrop-blur-lg border border-cyan-500/30 p-3 rounded-lg">
          <div className="text-xs text-slate-400 space-y-1">
            <div>FPS: {fps}</div>
            <div>Zoom: {(zoomLevel * 100).toFixed(0)}%</div>
            <div>Pos: {cameraPosition.x.toFixed(1)}, {cameraPosition.y.toFixed(1)}</div>
            <div>Objects: {gameState?.galaxy.length || 0}</div>
          </div>
        </div>
      )}
    </div>
  );
}