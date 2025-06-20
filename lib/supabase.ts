import { createClient } from '@supabase/supabase-js'

// Supabase URL ve Anon Key
const supabaseUrl = process.env.NEXT_PUBLIC_SUPABASE_URL!
const supabaseAnonKey = process.env.NEXT_PUBLIC_SUPABASE_ANON_KEY!

// Supabase client oluştur
export const supabase = createClient(supabaseUrl, supabaseAnonKey)

// Database schema türleri
export interface Database {
  public: {
    Tables: {
      users: {
        Row: {
          id: string
          username: string
          email: string
          created_at: string
          updated_at: string
          level: number
          experience: number
          diamonds: number
          gold: number
          premium_until?: string
          avatar_url?: string
          rank: string
          alliance_id?: string
        }
        Insert: {
          id?: string
          username: string
          email: string
          created_at?: string
          updated_at?: string
          level?: number
          experience?: number
          diamonds?: number
          gold?: number
          premium_until?: string
          avatar_url?: string
          rank?: string
          alliance_id?: string
        }
        Update: {
          id?: string
          username?: string
          email?: string
          created_at?: string
          updated_at?: string
          level?: number
          experience?: number
          diamonds?: number
          gold?: number
          premium_until?: string
          avatar_url?: string
          rank?: string
          alliance_id?: string
        }
      }
      planets: {
        Row: {
          id: string
          user_id: string
          name: string
          planet_type: 'Terran' | 'Çöl' | 'Buz' | 'Volkanik' | 'Gaz'
          level: number
          coordinates_galaxy: number
          coordinates_system: number
          coordinates_position: number
          temperature: number
          population: number
          defense_points: number
          metal: number
          crystal: number
          deuterium: number
          energy: number
          created_at: string
          updated_at: string
        }
        Insert: {
          id?: string
          user_id: string
          name: string
          planet_type: 'Terran' | 'Çöl' | 'Buz' | 'Volkanik' | 'Gaz'
          level?: number
          coordinates_galaxy: number
          coordinates_system: number
          coordinates_position: number
          temperature?: number
          population?: number
          defense_points?: number
          metal?: number
          crystal?: number
          deuterium?: number
          energy?: number
          created_at?: string
          updated_at?: string
        }
        Update: {
          id?: string
          user_id?: string
          name?: string
          planet_type?: 'Terran' | 'Çöl' | 'Buz' | 'Volkanik' | 'Gaz'
          level?: number
          coordinates_galaxy?: number
          coordinates_system?: number
          coordinates_position?: number
          temperature?: number
          population?: number
          defense_points?: number
          metal?: number
          crystal?: number
          deuterium?: number
          energy?: number
          created_at?: string
          updated_at?: string
        }
      }
      buildings: {
        Row: {
          id: string
          planet_id: string
          building_type: string
          level: number
          upgrading: boolean
          upgrade_finish_time?: string
          created_at: string
          updated_at: string
        }
        Insert: {
          id?: string
          planet_id: string
          building_type: string
          level?: number
          upgrading?: boolean
          upgrade_finish_time?: string
          created_at?: string
          updated_at?: string
        }
        Update: {
          id?: string
          planet_id?: string
          building_type?: string
          level?: number
          upgrading?: boolean
          upgrade_finish_time?: string
          created_at?: string
          updated_at?: string
        }
      }
      fleets: {
        Row: {
          id: string
          user_id: string
          planet_id: string
          name: string
          mission_type: string
          destination_galaxy?: number
          destination_system?: number
          destination_position?: number
          departure_time: string
          arrival_time?: string
          return_time?: string
          status: 'Seyir' | 'Savaş' | 'Dönüş' | 'Yerleşik'
          created_at: string
          updated_at: string
        }
        Insert: {
          id?: string
          user_id: string
          planet_id: string
          name: string
          mission_type: string
          destination_galaxy?: number
          destination_system?: number
          destination_position?: number
          departure_time: string
          arrival_time?: string
          return_time?: string
          status?: 'Seyir' | 'Savaş' | 'Dönüş' | 'Yerleşik'
          created_at?: string
          updated_at?: string
        }
        Update: {
          id?: string
          user_id?: string
          planet_id?: string
          name?: string
          mission_type?: string
          destination_galaxy?: number
          destination_system?: number
          destination_position?: number
          departure_time?: string
          arrival_time?: string
          return_time?: string
          status?: 'Seyir' | 'Savaş' | 'Dönüş' | 'Yerleşik'
          created_at?: string
          updated_at?: string
        }
      }
      fleet_ships: {
        Row: {
          id: string
          fleet_id: string
          ship_type: string
          quantity: number
          created_at: string
        }
        Insert: {
          id?: string
          fleet_id: string
          ship_type: string
          quantity: number
          created_at?: string
        }
        Update: {
          id?: string
          fleet_id?: string
          ship_type?: string
          quantity?: number
          created_at?: string
        }
      }
      technologies: {
        Row: {
          id: string
          user_id: string
          tech_type: string
          level: number
          researching: boolean
          research_finish_time?: string
          created_at: string
          updated_at: string
        }
        Insert: {
          id?: string
          user_id: string
          tech_type: string
          level?: number
          researching?: boolean
          research_finish_time?: string
          created_at?: string
          updated_at?: string
        }
        Update: {
          id?: string
          user_id?: string
          tech_type?: string
          level?: number
          researching?: boolean
          research_finish_time?: string
          created_at?: string
          updated_at?: string
        }
      }
      battles: {
        Row: {
          id: string
          attacker_id: string
          defender_id: string
          planet_id: string
          battle_type: 'Saldırı' | 'Savunma' | 'Casusluk'
          start_time: string
          end_time?: string
          result?: 'Saldırgan Kazandı' | 'Savunmacı Kazandı' | 'Berabere'
          attacker_losses?: number
          defender_losses?: number
          resources_looted?: number
          created_at: string
        }
        Insert: {
          id?: string
          attacker_id: string
          defender_id: string
          planet_id: string
          battle_type: 'Saldırı' | 'Savunma' | 'Casusluk'
          start_time: string
          end_time?: string
          result?: 'Saldırgan Kazandı' | 'Savunmacı Kazandı' | 'Berabere'
          attacker_losses?: number
          defender_losses?: number
          resources_looted?: number
          created_at?: string
        }
        Update: {
          id?: string
          attacker_id?: string
          defender_id?: string
          planet_id?: string
          battle_type?: 'Saldırı' | 'Savunma' | 'Casusluk'
          start_time?: string
          end_time?: string
          result?: 'Saldırgan Kazandı' | 'Savunmacı Kazandı' | 'Berabere'
          attacker_losses?: number
          defender_losses?: number
          resources_looted?: number
          created_at?: string
        }
      }
      alliances: {
        Row: {
          id: string
          name: string
          tag: string
          description?: string
          leader_id: string
          member_count: number
          total_points: number
          created_at: string
          updated_at: string
        }
        Insert: {
          id?: string
          name: string
          tag: string
          description?: string
          leader_id: string
          member_count?: number
          total_points?: number
          created_at?: string
          updated_at?: string
        }
        Update: {
          id?: string
          name?: string
          tag?: string
          description?: string
          leader_id?: string
          member_count?: number
          total_points?: number
          created_at?: string
          updated_at?: string
        }
      }
    }
    Views: {
      [_ in never]: never
    }
    Functions: {
      [_ in never]: never
    }
    Enums: {
      [_ in never]: never
    }
  }
}

// Typed Supabase client
export type SupabaseClient = typeof supabase
export type Tables<T extends keyof Database['public']['Tables']> = Database['public']['Tables'][T]['Row']
