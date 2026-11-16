# YouTube Data API Integration

Base URL: `https://www.googleapis.com/youtube/v3/`. The API key is added to every request by `ApiKeyInterceptor`.

## Endpoints

| # | Endpoint | Purpose | Cost |
|---|---|---|---|
| 1 | `channels?forHandle=@LovinDubai&part=snippet,statistics,contentDetails` | Channel info, statistics, and the uploads playlist ID | 1 unit |
| 2 | `playlistItems?playlistId={uploadsId}&part=contentDetails&maxResults=50&pageToken={token}` | 50 video IDs per page, plus `nextPageToken` | 1 unit |
| 3 | `videos?id={up to 50 ids}&part=snippet,contentDetails,statistics,liveStreamingDetails` | Full details for all videos on the page | 1 unit |

- `videos.list` costs 1 unit no matter how many parts are requested, so all needed parts come in one call.
- `pageToken` is omitted for the first page. A `null` `nextPageToken` means the last page has been reached.

## Quota Usage

| Action | Cost |
|---|---|
| First load (channel + first 50 videos) | 3 units |
| Each next page | 2 units |
| Pull-to-refresh | 3 units |
| Cache less than 1 hour old, switching tabs, or opening details | 0 units |

A single `search.list` request costs 100 units.

## Classification

Each video is classified once, when it is saved to Room:

```text
liveBroadcastContent == "live"      → LIVE (ACTIVE)
liveBroadcastContent == "upcoming"  → LIVE (UPCOMING)
liveStreamingDetails present        → LIVE (COMPLETED)
duration ≤ 180s (60s before Oct 2024) → SHORT
otherwise                           → REGULAR
```

## Error Handling

| Case | Result |
|---|---|
| No internet | "No internet connection" message; cached content stays visible |
| HTTP 403 `quotaExceeded` | Quota message; cached content stays visible |
| Other API errors | Generic error with Retry |
| Deleted or private video | Not returned by `videos.list`, so it is not stored |
| Missing fields | Nullable DTOs with safe defaults |
