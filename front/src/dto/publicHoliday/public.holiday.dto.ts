export interface PublicHolidayRequestDTO {
  date: string; // Format de date tel que "YYYY-MM-DD"
  label: string;
}

export interface PublicHolidayResponseDTO {
  publicId: string;
  date: string; // Format de date tel que "YYYY-MM-DD"
  label: string;
}