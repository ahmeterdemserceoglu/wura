"use client";

import { useState, useEffect } from "react";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { Progress } from "@/components/ui/progress";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog";
import { Tooltip, TooltipContent, TooltipProvider, TooltipTrigger } from "@/components/ui/tooltip";
import { Alert, AlertDescription } from "@/components/ui/alert";
import { Slider } from "@/components/ui/slider";
import {
  Rocket,
  Settings,
  Users,
  Shield,
  Zap,
  Coins,
  Diamond,
  Hammer,
  Gem,
  Globe,
  Sword,
  Building,
  FlaskConical,
  Ship,
  Crown,
  Bell,
  Menu,
  Star,
  Target,
  TrendingUp,
  Crosshair,
  Flame,
  Activity,
  AlertTriangle,
  Clock,
  Eye,
  Wifi,
  Volume2,
  VolumeX,
  Maximize,
  Radio,
  Radar,
  Cpu,
  Database,
  Power,
  ChevronRight,
  Sparkles,
  Swords,
  Timer,
  Map
} from "lucide-react";

// Oyun verileri türleri
interface Planet {
  id: string;
  name: string;
  type: 'Terran' | 'Çöl' | 'Buz' | 'Volkanik' | 'Gaz';
  level: number;
  coordinates: string;
  temperature: number;
  population: number;
  defense: number;
  image: string;
}

interface Building {
  id: string;
  name: string;
  level: number;
  maxLevel: number;
  upgrading: boolean;
  upgradeTimeLeft?: number;
  production: number;
  energyUsage: number;
  description: string;
}

interface Fleet {
  id: string;
  name: string;
  ships: number;
  mission: string;
  destination?: string;
  eta?: string;
  status: 'Seyir' | 'Savaş' | 'Dönüş' | 'Yerleşik';
  strength: number;
}

interface Battle {
  id: string;
  attacker: string;
  defender: string;
  planet: string;
  eta: string;
  type: 'Saldırı' | 'Savunma' | 'Casusluk';
  intensity: number;
}

export default function GalacticEmpire() {
  const [selectedPlanet, setSelectedPlanet] = useState("Ana Dünya");
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);
  const [stars, setStars] = useState<Array<{id: string, left: string, top: string, animationDelay: string, animationDuration: string}>>([]);
  const [currentTime, setCurrentTime] = useState(new Date());
  const [isConnected, setIsConnected] = useState(true);
  const [soundEnabled, setSoundEnabled] = useState(true);
  const [activeTab, setActiveTab] = useState("genel-bakis");

  // Real-time saat güncelleme
  useEffect(() => {
    const timer = setInterval(() => setCurrentTime(new Date()), 1000);
    return () => clearInterval(timer);
  }, []);

  useEffect(() => {
    // Yıldız alanı oluştur
    const generatedStars = Array.from({ length: 100 }, (_, i) => ({
      id: `yildiz-${i}-${Date.now()}`,
      left: `${Math.random() * 100}%`,
      top: `${Math.random() * 100}%`,
      animationDelay: `${Math.random() * 5}s`,
      animationDuration: `${3 + Math.random() * 4}s`
    }));
    setStars(generatedStars);
  }, []);

  // Format numbers consistently
  const formatNumber = (num: number) => {
    return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, '.');
  };

  // Oyun verileri
  const resources = {
    metal: { amount: 2847632, production: 1247, storage: 5000000, name: "Metal" },
    crystal: { amount: 1567820, production: 823, storage: 3000000, name: "Kristal" },
    deuterium: { amount: 634560, production: 389, storage: 2000000, name: "Deuterium" },
    energy: { amount: 8340, production: 9500, consumption: 1160, name: "Enerji" },
    gold: { amount: 47583, production: 0, storage: 999999, name: "Altın" },
    diamonds: { amount: 247, production: 0, storage: 999999, name: "Elmas" }
  };

  const planets: Planet[] = [
    {
      id: '1',
      name: "Ana Dünya",
      type: "Terran",
      level: 28,
      coordinates: "[1:234:7]",
      temperature: 22,
      population: 12847632,
      defense: 8500,
      image: "/planet-terran.jpg"
    },
    {
      id: '2',
      name: "Mars Kolonisi",
      type: "Çöl",
      level: 22,
      coordinates: "[1:234:12]",
      temperature: -18,
      population: 4567234,
      defense: 3200,
      image: "/planet-desert.jpg"
    },
    {
      id: '3',
      name: "Europa Üssü",
      type: "Buz",
      level: 18,
      coordinates: "[2:145:3]",
      temperature: -89,
      population: 1234567,
      defense: 1800,
      image: "/planet-ice.jpg"
    }
  ];

  const buildings: Building[] = [
    {
      id: '1',
      name: "Metal Madeni",
      level: 28,
      maxLevel: 50,
      upgrading: false,
      production: 1247,
      energyUsage: 234,
      description: "Metal üretimi yapan ana tesis"
    },
    {
      id: '2',
      name: "Kristal Madeni",
      level: 25,
      maxLevel: 45,
      upgrading: true,
      upgradeTimeLeft: 3672,
      production: 823,
      energyUsage: 187,
      description: "Yüksek teknoloji için kristal üretir"
    },
    {
      id: '3',
      name: "Deuterium Sentezleyici",
      level: 24,
      maxLevel: 40,
      upgrading: false,
      production: 389,
      energyUsage: 156,
      description: "Gemi yakıtı üretimi"
    },
    {
      id: '4',
      name: "Güneş Enerji Santrali",
      level: 30,
      maxLevel: 60,
      upgrading: false,
      production: 2450,
      energyUsage: 0,
      description: "Temiz enerji kaynağı"
    },
    {
      id: '5',
      name: "Robotik Fabrika",
      level: 18,
      maxLevel: 25,
      upgrading: false,
      production: 0,
      energyUsage: 89,
      description: "İnşaat hızını artırır"
    },
    {
      id: '6',
      name: "Gemi Tersanesi",
      level: 16,
      maxLevel: 30,
      upgrading: false,
      production: 0,
      energyUsage: 124,
      description: "Savaş gemileri üretir"
    }
  ];

  const fleets: Fleet[] = [
    {
      id: '1',
      name: "Alpha Saldırı Filosu",
      ships: 347,
      mission: "Saldırı",
      destination: "Hedef: [3:456:8]",
      eta: "2s 47dk",
      status: "Seyir",
      strength: 89500
    },
    {
      id: '2',
      name: "Beta Savunma Skuadronu",
      ships: 189,
      mission: "Savunma",
      status: "Yerleşik",
      strength: 45600
    },
    {
      id: '3',
      name: "Gama Keşif Kanadı",
      ships: 24,
      mission: "Casusluk",
      destination: "Hedef: [1:234:9]",
      eta: "58dk",
      status: "Seyir",
      strength: 2400
    },
    {
      id: '4',
      name: "Delta Nakliye Konvoyu",
      ships: 67,
      mission: "Nakliye",
      destination: "Mars Kolonisi",
      eta: "1s 23dk",
      status: "Dönüş",
      strength: 8900
    }
  ];

  const activeBattles: Battle[] = [
    {
      id: '1',
      attacker: "KomutanX",
      defender: "Sen",
      planet: "Ana Dünya",
      eta: "4dk 23s",
      type: "Saldırı",
      intensity: 85
    },
    {
      id: '2',
      attacker: "Sen",
      defender: "DüşmanY",
      planet: "[1:234:15]",
      eta: "12dk 47s",
      type: "Saldırı",
      intensity: 72
    }
  ];

  const technologies = [
    { name: "Silah Teknolojisi", level: 18, research: false },
    { name: "Kalkan Teknolojisi", level: 16, research: true },
    { name: "Zırh Teknolojisi", level: 14, research: false },
    { name: "Astrofizik", level: 12, research: false },
    { name: "Bilgisayar Teknolojisi", level: 20, research: false }
  ];

  const formatTime = (seconds: number) => {
    const hours = Math.floor(seconds / 3600);
    const minutes = Math.floor((seconds % 3600) / 60);
    const secs = seconds % 60;
    return `${hours}s ${minutes}dk ${secs}s`;
  };

  return (
    <TooltipProvider>
      <div className="min-h-screen bg-gradient-to-br from-slate-900 via-blue-900 to-purple-900 relative overflow-hidden">
        {/* Advanced Starfield */}
        <div className="fixed inset-0 overflow-hidden pointer-events-none z-0">
          <div className="absolute inset-0 bg-[radial-gradient(ellipse_at_center,_var(--tw-gradient-stops))] from-transparent via-blue-900/10 to-slate-900/50" />
          {/* Nebula Effect */}
          <div className="absolute top-1/4 left-1/3 w-96 h-96 bg-purple-500/10 rounded-full blur-3xl animate-pulse" />
          <div className="absolute bottom-1/4 right-1/4 w-64 h-64 bg-blue-500/10 rounded-full blur-3xl animate-pulse" />

          {/* Stars */}
          {stars.map((star) => (
            <div
              key={star.id}
              className="absolute animate-pulse"
              style={{
                left: star.left,
                top: star.top,
                animationDelay: star.animationDelay,
                animationDuration: star.animationDuration
              }}
            >
              <Star className="w-1 h-1 text-white/40 fill-current" />
            </div>
          ))}
        </div>

        {/* Game Header */}
        <header className="relative z-10 border-b border-cyan-500/30 bg-slate-900/90 backdrop-blur-lg shadow-2xl">
          <div className="container mx-auto px-4 py-3">
            <div className="flex items-center justify-between">
              {/* Logo & Status */}
              <div className="flex items-center gap-6">
                <Button
                  variant="ghost"
                  size="icon"
                  className="md:hidden text-cyan-400 hover:bg-cyan-500/20"
                  onClick={() => setIsSidebarOpen(!isSidebarOpen)}
                >
                  <Menu className="h-5 w-5" />
                </Button>

                <div className="flex items-center gap-3">
                  <div className="relative">
                    <Rocket className="h-10 w-10 text-cyan-400 animate-pulse" />
                    <div className="absolute -top-1 -right-1 w-3 h-3 bg-green-400 rounded-full animate-ping" />
                  </div>
                  <div>
                    <h1 className="text-2xl font-bold bg-gradient-to-r from-cyan-400 to-blue-400 bg-clip-text text-transparent">
                      GALAKTİK İMPARATORLUK
                    </h1>
                    <div className="flex items-center gap-3 text-xs">
                      <span className="text-cyan-400">v2.1.47</span>
                      <div className="flex items-center gap-1">
                        <Wifi className={`h-3 w-3 ${isConnected ? 'text-green-400' : 'text-red-400'}`} />
                        <span className={isConnected ? 'text-green-400' : 'text-red-400'}>
                          {isConnected ? 'BAĞLİ' : 'BAĞLANTI YOK'}
                        </span>
                      </div>
                      <div className="flex items-center gap-1">
                        <Clock className="h-3 w-3 text-slate-400" />
                        <span className="text-slate-400">
                          {currentTime.toLocaleTimeString('tr-TR')}
                        </span>
                      </div>
                    </div>
                  </div>
                </div>
              </div>

              {/* Resource Display */}
              <div className="hidden lg:flex items-center gap-8">
                {Object.entries(resources).map(([key, resource]) => {
                  const icons = {
                    metal: Hammer,
                    crystal: Gem,
                    deuterium: Zap,
                    energy: Power,
                    gold: Coins,
                    diamonds: Diamond
                  };
                  const colors = {
                    metal: "text-orange-400",
                    crystal: "text-cyan-400",
                    deuterium: "text-green-400",
                    energy: "text-yellow-400",
                    gold: "text-amber-400",
                    diamonds: "text-blue-400"
                  };
                  const Icon = icons[key as keyof typeof icons];

                  return (
                    <Tooltip key={key}>
                      <TooltipTrigger>
                        <div className="flex items-center gap-2 px-3 py-2 rounded-lg bg-slate-800/50 border border-slate-700/50 hover:border-cyan-500/50 transition-all cursor-pointer">
                          <Icon className={`h-5 w-5 ${colors[key as keyof typeof colors]}`} />
                          <div className="text-right">
                            <div className="font-mono text-sm text-white">
                              {formatNumber(resource.amount)}
                            </div>
                            {resource.production > 0 && (
                              <div className="text-xs text-green-400">
                                +{formatNumber(resource.production)}/s
                              </div>
                            )}
                          </div>
                        </div>
                      </TooltipTrigger>
                      <TooltipContent>
                        <div className="text-center">
                          <div className="font-semibold">{resource.name}</div>
                          <div className="text-xs">Depo: {formatNumber(resource.storage)}</div>
                          {resource.production > 0 && (
                            <div className="text-xs text-green-400">
                              Üretim: +{formatNumber(resource.production)}/saat
                            </div>
                          )}
                        </div>
                      </TooltipContent>
                    </Tooltip>
                  );
                })}
              </div>

              {/* Controls */}
              <div className="flex items-center gap-3">
                <Button
                  variant="ghost"
                  size="icon"
                  className="text-cyan-400 hover:bg-cyan-500/20"
                  onClick={() => setSoundEnabled(!soundEnabled)}
                >
                  {soundEnabled ? <Volume2 className="h-5 w-5" /> : <VolumeX className="h-5 w-5" />}
                </Button>

                <div className="relative">
                  <Button variant="ghost" size="icon" className="text-cyan-400 hover:bg-cyan-500/20">
                    <Bell className="h-5 w-5" />
                  </Button>
                  {activeBattles.length > 0 && (
                    <div className="absolute -top-2 -right-2 w-5 h-5 bg-red-500 rounded-full text-xs text-white flex items-center justify-center animate-bounce">
                      {activeBattles.length}
                    </div>
                  )}
                </div>

                <Avatar className="ring-2 ring-cyan-500/50">
                  <AvatarImage src="/commander-avatar.png" />
                  <AvatarFallback className="bg-cyan-600 text-white">K1</AvatarFallback>
                </Avatar>

                <div className="hidden md:block">
                  <p className="text-sm font-medium text-cyan-400">Komutan Elite</p>
                  <p className="text-xs text-slate-400">Seviye 47 • İmparatorluk Rütbesi</p>
                </div>
              </div>
            </div>
          </div>
        </header>

        <div className="relative z-10 flex">
          {/* Game Sidebar */}
          <aside className={`
            fixed md:relative inset-y-0 left-0 z-20 w-80 bg-slate-900/95 backdrop-blur-lg border-r border-cyan-500/30
            transform transition-transform duration-300 ease-in-out shadow-2xl
            ${isSidebarOpen ? 'translate-x-0' : '-translate-x-full md:translate-x-0'}
          `}>
            <div className="p-6 h-full overflow-y-auto">
              {/* Planet Selector */}
              <div className="mb-8">
                <h3 className="text-sm font-medium text-cyan-400 mb-3 flex items-center gap-2">
                  <Globe className="h-4 w-4" />
                  AKTIF GEZEGEN
                </h3>
                <Card className="bg-slate-800/70 border-cyan-500/30 hover:border-cyan-500/60 transition-all cursor-pointer">
                  <CardContent className="p-4">
                    <div className="flex items-center gap-3">
                      <div className="relative">
                        <Globe className="h-8 w-8 text-cyan-400" />
                        <div className="absolute -bottom-1 -right-1 w-3 h-3 bg-green-400 rounded-full" />
                      </div>
                      <div className="flex-1">
                        <div className="font-semibold text-white">{selectedPlanet}</div>
                        <div className="text-xs text-slate-400">[1:234:7] • Terran Dünya</div>
                        <div className="flex items-center gap-2 mt-1">
                          <Shield className="h-3 w-3 text-green-400" />
                          <span className="text-xs text-green-400">Savunma: 8.500</span>
                        </div>
                      </div>
                      <ChevronRight className="h-4 w-4 text-slate-400" />
                    </div>
                  </CardContent>
                </Card>
              </div>

              {/* Navigation Menu */}
              <nav className="space-y-2">
                <div className="text-xs font-medium text-cyan-400 mb-3 uppercase tracking-wider">
                  KOMUT MERKEZİ
                </div>

                {[
                  { id: 'genel-bakis', icon: Activity, label: 'Genel Bakış', active: true },
                  { id: 'binalar', icon: Building, label: 'Binalar', badge: '2' },
                  { id: 'arastirma', icon: FlaskConical, label: 'Araştırma', badge: '1' },
                  { id: 'tersane', icon: Ship, label: 'Gemi Tersanesi' },
                  { id: 'savunma', icon: Shield, label: 'Savunma Sistemi' },
                  { id: 'galaksi', icon: Map, label: 'Galaksi Haritası' },
                  { id: 'ittifak', icon: Users, label: 'İttifak' },
                  { id: 'savaşlar', icon: Swords, label: 'Aktif Savaşlar', badge: activeBattles.length.toString() },
                  { id: 'istatistik', icon: TrendingUp, label: 'İstatistikler' }
                ].map((item) => (
                  <Button
                    key={item.id}
                    variant="ghost"
                    className={`w-full justify-start text-left h-12 ${
                      item.active
                        ? 'bg-cyan-600/30 text-cyan-400 border border-cyan-500/50'
                        : 'text-slate-300 hover:text-cyan-400 hover:bg-cyan-500/10'
                    }`}
                    onClick={() => setActiveTab(item.id)}
                  >
                    <item.icon className="mr-3 h-5 w-5" />
                    <span className="flex-1">{item.label}</span>
                    {item.badge && (
                      <Badge variant="destructive" className="bg-red-500/80 text-white text-xs">
                        {item.badge}
                      </Badge>
                    )}
                  </Button>
                ))}
              </nav>

              {/* Active Battles Alert */}
              {activeBattles.length > 0 && (
                <div className="mt-8">
                  <Alert className="border-red-500/50 bg-red-500/10">
                    <AlertTriangle className="h-4 w-4 text-red-400" />
                    <AlertDescription className="text-red-400">
                      <div className="font-semibold mb-1">AKTİF SAVAŞ UYARISI!</div>
                      <div className="text-xs">
                        {activeBattles.length} aktif savaş devam ediyor
                      </div>
                    </AlertDescription>
                  </Alert>
                </div>
              )}
            </div>
          </aside>

          {/* Main Game Content */}
          <main className="flex-1 p-6 space-y-6 min-h-screen">
            {/* Active Battles Bar */}
            {activeBattles.length > 0 && (
              <div className="bg-red-500/10 border border-red-500/30 rounded-lg p-4">
                <div className="flex items-center justify-between mb-3">
                  <h3 className="text-red-400 font-semibold flex items-center gap-2">
                    <Flame className="h-5 w-5" />
                    AKTİF SAVAŞLAR
                  </h3>
                  <Badge variant="destructive">{activeBattles.length} Savaş</Badge>
                </div>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
                  {activeBattles.map((battle) => (
                    <div key={battle.id} className="bg-slate-800/50 rounded-lg p-3 border border-red-500/20">
                      <div className="flex items-center justify-between mb-2">
                        <span className="text-sm font-medium text-white">
                          {battle.type === 'Saldırı' ? '⚔️' : '🛡️'} {battle.attacker} vs {battle.defender}
                        </span>
                        <span className="text-xs text-red-400 font-mono">{battle.eta}</span>
                      </div>
                      <div className="flex items-center gap-2">
                        <span className="text-xs text-slate-400">Hedef:</span>
                        <span className="text-xs text-cyan-400">{battle.planet}</span>
                      </div>
                      <Progress value={battle.intensity} className="mt-2 h-2" />
                    </div>
                  ))}
                </div>
              </div>
            )}

            {/* Mobile Resources */}
            <div className="lg:hidden grid grid-cols-3 gap-3">
              {Object.entries(resources).map(([key, resource]) => {
                const icons = {
                  metal: Hammer,
                  crystal: Gem,
                  deuterium: Zap,
                  energy: Power,
                  gold: Coins,
                  diamonds: Diamond
                };
                const colors = {
                  metal: "text-orange-400",
                  crystal: "text-cyan-400",
                  deuterium: "text-green-400",
                  energy: "text-yellow-400",
                  gold: "text-amber-400",
                  diamonds: "text-blue-400"
                };
                const Icon = icons[key as keyof typeof icons];

                return (
                  <Card key={key} className="bg-slate-800/50 border-slate-700/50">
                    <CardContent className="p-3">
                      <div className="flex items-center gap-2">
                        <Icon className={`h-4 w-4 ${colors[key as keyof typeof colors]}`} />
                        <div>
                          <div className="text-xs font-mono text-white">
                            {formatNumber(resource.amount)}
                          </div>
                          {resource.production > 0 && (
                            <div className="text-xs text-green-400">
                              +{resource.production}/s
                            </div>
                          )}
                        </div>
                      </div>
                    </CardContent>
                  </Card>
                );
              })}
            </div>

            {/* Main Dashboard */}
            <div className="grid grid-cols-1 xl:grid-cols-4 gap-6">
              {/* Central Game Panel */}
              <div className="xl:col-span-3 space-y-6">
                {/* Planet Overview */}
                <Card className="bg-slate-800/50 border-cyan-500/30">
                  <CardHeader>
                    <CardTitle className="flex items-center justify-between text-cyan-400">
                      <div className="flex items-center gap-3">
                        <Globe className="h-6 w-6" />
                        GEZEGEN KOMPLEKSİ
                      </div>
                      <Button variant="outline" size="sm" className="border-cyan-500/50 text-cyan-400">
                        <Eye className="h-4 w-4 mr-2" />
                        Detaylı Görünüm
                      </Button>
                    </CardTitle>
                  </CardHeader>
                  <CardContent>
                    <Tabs value={activeTab} onValueChange={setActiveTab} className="w-full">
                      <TabsList className="grid w-full grid-cols-4 bg-slate-700/50 mb-6">
                        <TabsTrigger value="binalar" className="data-[state=active]:bg-cyan-600 data-[state=active]:text-white">
                          🏗️ Binalar
                        </TabsTrigger>
                        <TabsTrigger value="filosu" className="data-[state=active]:bg-cyan-600 data-[state=active]:text-white">
                          🚀 Filolar
                        </TabsTrigger>
                        <TabsTrigger value="savunma" className="data-[state=active]:bg-cyan-600 data-[state=active]:text-white">
                          🛡️ Savunma
                        </TabsTrigger>
                        <TabsTrigger value="arastirma" className="data-[state=active]:bg-cyan-600 data-[state=active]:text-white">
                          🔬 Araştırma
                        </TabsTrigger>
                      </TabsList>

                      <TabsContent value="binalar" className="space-y-4">
                        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                          {buildings.map((building) => (
                            <Card key={building.id} className="bg-slate-700/30 border-slate-600/50 hover:border-cyan-500/50 transition-all">
                              <CardContent className="p-4">
                                <div className="flex items-center justify-between mb-3">
                                  <div className="flex items-center gap-3">
                                    <Building className="h-6 w-6 text-orange-400" />
                                    <div>
                                      <div className="font-semibold text-white">{building.name}</div>
                                      <div className="text-xs text-slate-400">
                                        Seviye {building.level}/{building.maxLevel}
                                      </div>
                                    </div>
                                  </div>
                                  {building.upgrading && (
                                    <Badge className="bg-yellow-600/20 text-yellow-400 animate-pulse">
                                      ⚡ Gelişiyor
                                    </Badge>
                                  )}
                                </div>

                                <div className="space-y-2 text-xs">
                                  <div className="flex justify-between">
                                    <span className="text-slate-400">Üretim:</span>
                                    <span className="text-green-400">+{formatNumber(building.production)}/s</span>
                                  </div>
                                  <div className="flex justify-between">
                                    <span className="text-slate-400">Enerji:</span>
                                    <span className="text-yellow-400">-{building.energyUsage}/s</span>
                                  </div>
                                </div>

                                {building.upgrading && building.upgradeTimeLeft && (
                                  <div className="mt-3">
                                    <div className="flex justify-between text-xs mb-1">
                                      <span className="text-slate-400">Kalan Süre:</span>
                                      <span className="text-yellow-400">{formatTime(building.upgradeTimeLeft)}</span>
                                    </div>
                                    <Progress value={(3600 - building.upgradeTimeLeft) / 3600 * 100} className="h-2" />
                                  </div>
                                )}

                                <div className="flex gap-2 mt-4">
                                  <Button
                                    size="sm"
                                    className="flex-1 bg-cyan-600 hover:bg-cyan-700 text-white"
                                    disabled={building.upgrading || building.level >= building.maxLevel}
                                  >
                                    <ChevronRight className="h-4 w-4 mr-1" />
                                    Geliştir
                                  </Button>
                                  <Tooltip>
                                    <TooltipTrigger asChild>
                                      <Button variant="outline" size="sm" className="border-slate-600">
                                        <Eye className="h-4 w-4" />
                                      </Button>
                                    </TooltipTrigger>
                                    <TooltipContent>
                                      <p>{building.description}</p>
                                    </TooltipContent>
                                  </Tooltip>
                                </div>
                              </CardContent>
                            </Card>
                          ))}
                        </div>
                      </TabsContent>

                      <TabsContent value="filosu" className="space-y-4">
                        {fleets.map((fleet) => (
                          <Card key={fleet.id} className="bg-slate-700/30 border-slate-600/50">
                            <CardContent className="p-4">
                              <div className="flex items-center justify-between mb-3">
                                <div className="flex items-center gap-3">
                                  <Ship className="h-6 w-6 text-cyan-400" />
                                  <div>
                                    <div className="font-semibold text-white">{fleet.name}</div>
                                    <div className="text-xs text-slate-400">
                                      {fleet.ships} gemi • {fleet.mission}
                                    </div>
                                  </div>
                                </div>
                                <Badge
                                  className={
                                    fleet.status === 'Seyir' ? 'bg-blue-600/20 text-blue-400' :
                                    fleet.status === 'Savaş' ? 'bg-red-600/20 text-red-400' :
                                    fleet.status === 'Dönüş' ? 'bg-green-600/20 text-green-400' :
                                    'bg-slate-600/20 text-slate-400'
                                  }
                                >
                                  {fleet.status}
                                </Badge>
                              </div>

                              <div className="grid grid-cols-2 gap-4 text-xs mb-4">
                                <div>
                                  <span className="text-slate-400">Güç:</span>
                                  <span className="text-cyan-400 ml-2">{formatNumber(fleet.strength)}</span>
                                </div>
                                {fleet.destination && (
                                  <div>
                                    <span className="text-slate-400">Hedef:</span>
                                    <span className="text-yellow-400 ml-2">{fleet.destination}</span>
                                  </div>
                                )}
                                {fleet.eta && (
                                  <div>
                                    <span className="text-slate-400">Varış:</span>
                                    <span className="text-green-400 ml-2">{fleet.eta}</span>
                                  </div>
                                )}
                              </div>

                              <div className="flex gap-2">
                                <Button size="sm" variant="outline" className="flex-1 border-slate-600 text-slate-300">
                                  <Target className="h-4 w-4 mr-1" />
                                  Komut Ver
                                </Button>
                                <Button size="sm" variant="outline" className="border-slate-600 text-slate-300">
                                  <Eye className="h-4 w-4" />
                                </Button>
                              </div>
                            </CardContent>
                          </Card>
                        ))}
                      </TabsContent>

                      <TabsContent value="savunma" className="space-y-4">
                        <div className="text-center py-12 text-slate-400">
                          <Shield className="h-16 w-16 mx-auto mb-4 opacity-50" />
                          <h3 className="text-lg font-semibold mb-2">Savunma Sistemleri</h3>
                          <p className="mb-4">Henüz savunma yapısı inşa edilmemiş.</p>
                          <Button className="bg-cyan-600 hover:bg-cyan-700 text-white">
                            <Building className="h-4 w-4 mr-2" />
                            Savunma İnşa Et
                          </Button>
                        </div>
                      </TabsContent>

                      <TabsContent value="arastirma" className="space-y-4">
                        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                          {technologies.map((tech, index) => (
                            <Card key={index} className="bg-slate-700/30 border-slate-600/50">
                              <CardContent className="p-4">
                                <div className="flex items-center justify-between mb-3">
                                  <div className="flex items-center gap-3">
                                    <FlaskConical className="h-5 w-5 text-purple-400" />
                                    <div>
                                      <div className="font-medium text-white">{tech.name}</div>
                                      <div className="text-xs text-slate-400">Seviye {tech.level}</div>
                                    </div>
                                  </div>
                                  {tech.research && (
                                    <Badge className="bg-purple-600/20 text-purple-400 animate-pulse">
                                      🔬 Araştırılıyor
                                    </Badge>
                                  )}
                                </div>
                                <Button
                                  size="sm"
                                  className="w-full bg-purple-600 hover:bg-purple-700 text-white"
                                  disabled={tech.research}
                                >
                                  {tech.research ? 'Araştırılıyor...' : 'Araştır'}
                                </Button>
                              </CardContent>
                            </Card>
                          ))}
                        </div>
                      </TabsContent>
                    </Tabs>
                  </CardContent>
                </Card>
              </div>

              {/* Right Panel */}
              <div className="space-y-6">
                {/* Empire Status */}
                <Card className="bg-slate-800/50 border-cyan-500/30">
                  <CardHeader>
                    <CardTitle className="flex items-center gap-2 text-cyan-400">
                      <Crown className="h-5 w-5" />
                      İMPARATORLUK
                    </CardTitle>
                  </CardHeader>
                  <CardContent className="space-y-4">
                    <div>
                      <div className="flex justify-between text-sm mb-2">
                        <span className="text-slate-400">Toplam Puan</span>
                        <span className="text-white font-mono">8.472.639</span>
                      </div>
                      <Progress value={84} className="h-3" />
                    </div>
                    <div>
                      <div className="flex justify-between text-sm mb-2">
                        <span className="text-slate-400">Galaksi Sıralaması</span>
                        <span className="text-cyan-400 font-mono">#847</span>
                      </div>
                      <Progress value={65} className="h-3" />
                    </div>
                    <div>
                      <div className="flex justify-between text-sm mb-2">
                        <span className="text-slate-400">İttifak Sıralaması</span>
                        <span className="text-green-400 font-mono">#12</span>
                      </div>
                      <Progress value={92} className="h-3" />
                    </div>
                  </CardContent>
                </Card>

                {/* Quick Actions */}
                <Card className="bg-slate-800/50 border-cyan-500/30">
                  <CardHeader>
                    <CardTitle className="text-cyan-400">HIZLI KOMUTLAR</CardTitle>
                  </CardHeader>
                  <CardContent className="space-y-3">
                    <Button className="w-full justify-start bg-gradient-to-r from-red-600 to-orange-600 hover:from-red-700 hover:to-orange-700 text-white">
                      <Crosshair className="mr-3 h-5 w-5" />
                      Saldırı Başlat
                    </Button>
                    <Button variant="outline" className="w-full justify-start border-cyan-500/50 text-cyan-400 hover:bg-cyan-500/10">
                      <Ship className="mr-3 h-5 w-5" />
                      Filo Gönder
                    </Button>
                    <Button variant="outline" className="w-full justify-start border-cyan-500/50 text-cyan-400 hover:bg-cyan-500/10">
                      <Building className="mr-3 h-5 w-5" />
                      Hızlı İnşa
                    </Button>
                    <Button variant="outline" className="w-full justify-start border-cyan-500/50 text-cyan-400 hover:bg-cyan-500/10">
                      <Radar className="mr-3 h-5 w-5" />
                      Galaksi Tara
                    </Button>
                    <Button variant="outline" className="w-full justify-start border-cyan-500/50 text-cyan-400 hover:bg-cyan-500/10">
                      <Users className="mr-3 h-5 w-5" />
                      İttifak Merkezi
                    </Button>
                  </CardContent>
                </Card>

                {/* System Status */}
                <Card className="bg-slate-800/50 border-cyan-500/30">
                  <CardHeader>
                    <CardTitle className="flex items-center gap-2 text-cyan-400">
                      <Cpu className="h-5 w-5" />
                      SİSTEM DURUMU
                    </CardTitle>
                  </CardHeader>
                  <CardContent className="space-y-3">
                    <div className="flex items-center justify-between">
                      <span className="text-sm text-slate-400">Sunucu</span>
                      <div className="flex items-center gap-2">
                        <div className="w-2 h-2 bg-green-400 rounded-full animate-pulse" />
                        <span className="text-xs text-green-400">Aktif</span>
                      </div>
                    </div>
                    <div className="flex items-center justify-between">
                      <span className="text-sm text-slate-400">Veritabanı</span>
                      <div className="flex items-center gap-2">
                        <div className="w-2 h-2 bg-green-400 rounded-full animate-pulse" />
                        <span className="text-xs text-green-400">Bağlı</span>
                      </div>
                    </div>
                    <div className="flex items-center justify-between">
                      <span className="text-sm text-slate-400">Galaksi Ağı</span>
                      <div className="flex items-center gap-2">
                        <div className="w-2 h-2 bg-yellow-400 rounded-full animate-pulse" />
                        <span className="text-xs text-yellow-400">Yoğun</span>
                      </div>
                    </div>
                    <div className="flex items-center justify-between">
                      <span className="text-sm text-slate-400">Oyuncu</span>
                      <span className="text-xs text-cyan-400">47.832 çevrimiçi</span>
                    </div>
                  </CardContent>
                </Card>
              </div>
            </div>
          </main>
        </div>

        {/* Mobile Overlay */}
        {isSidebarOpen && (
          <div
            className="fixed inset-0 bg-black/70 z-10 md:hidden backdrop-blur-sm"
            onClick={() => setIsSidebarOpen(false)}
          />
        )}
      </div>
    </TooltipProvider>
  );
}
