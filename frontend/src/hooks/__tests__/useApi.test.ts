import { renderHook, act } from '@testing-library/react';
import { useApi } from '../useApi';

describe('useApi', () => {
  it('should handle successful API call', async () => {
    const mockData = { id: 1, name: 'Test' };
    const mockApiCall = jest.fn().mockResolvedValue(mockData);

    const { result } = renderHook(() => useApi<typeof mockData>());

    expect(result.current.loading).toBe(false);
    expect(result.current.error).toBeNull();
    expect(result.current.data).toBeNull();

    await act(async () => {
      await result.current.execute(mockApiCall);
    });

    expect(result.current.loading).toBe(false);
    expect(result.current.error).toBeNull();
    expect(result.current.data).toEqual(mockData);
  });

  it('should handle API call error', async () => {
    const mockError = new Error('API Error');
    const mockApiCall = jest.fn().mockRejectedValue(mockError);

    const { result } = renderHook(() => useApi());

    await act(async () => {
      try {
        await result.current.execute(mockApiCall);
      } catch (error) {
        // Expected error
      }
    });

    expect(result.current.loading).toBe(false);
    expect(result.current.error).toBe('API Error');
    expect(result.current.data).toBeNull();
  });

  it('should reset state', async () => {
    const mockData = { id: 1, name: 'Test' };
    const mockApiCall = jest.fn().mockResolvedValue(mockData);

    const { result } = renderHook(() => useApi<typeof mockData>());

    await act(async () => {
      await result.current.execute(mockApiCall);
    });

    expect(result.current.data).toEqual(mockData);

    act(() => {
      result.current.reset();
    });

    expect(result.current.loading).toBe(false);
    expect(result.current.error).toBeNull();
    expect(result.current.data).toBeNull();
  });
}); 